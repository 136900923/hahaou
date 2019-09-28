package com.up72.task;

import com.up72.cnst.Cnst;
import com.up72.dao.CostDetailMapper;
import com.up72.dao.TemplateCostRelationMapper;
import com.up72.dto.CostDetailDto;
import com.up72.dto.TemplateCostRelationDto;
import com.up72.framework.util.TimeUtil;
import com.up72.model.CostDetail;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 费用详情定时任务
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class CostDetailTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostDetailTask.class);

    @Autowired
    private TemplateCostRelationMapper templateCostRelationMapper;

    @Autowired
    private CostDetailMapper costDetailMapper;

    /**
     * 插入年份数据
     */
    public void insertRecord() {

        LOGGER.info("插入年份数据");

        Map<String, Object> param = new HashMap<>();
        // 每家门店对应的业务视角包含的项目
        List<TemplateCostRelationDto> relatioinDtoList = templateCostRelationMapper.getDtoList(param);
//        LOGGER.info("模板对应的项目总数 : {}", relatioinDtoList.size());

        // 每家门店对应的业务视角中每个项目上年当年下年的统计数量
        List<CostDetailDto> recordList = costDetailMapper.getItemRecordTotal(param);
//        LOGGER.info("统计记录总数 : {}", recordList.size());

        Calendar curDate = Calendar.getInstance();
        int curYear = curDate.get(Calendar.YEAR);
        int lastYear = curYear - 1;
        int nextYear = curYear + 1;

        List<CostDetailDto> tempRecordList = new ArrayList<>();

        for (TemplateCostRelationDto itemDto : relatioinDtoList) {

            for (int i = lastYear; i <= nextYear; i++) {
                CostDetailDto dto = new CostDetailDto();
                dto.setStoreId(itemDto.getStoreId());
                dto.setTemplateId(itemDto.getTemplateId());
                dto.setVisualAngle(itemDto.getVisualAngle());
                dto.setItemId(itemDto.getItemId());
                dto.setYear(Long.valueOf(i));
                dto.setRecordTotal(0L);
                tempRecordList.add(dto);
            }

        }

//        LOGGER.info("tempRecordList : {}", JSON.toJSONString(tempRecordList));

        for (CostDetailDto dto : tempRecordList) {
            for (CostDetailDto recordDto : recordList) {
                if (dto.getStoreId().equals(recordDto.getStoreId())
                        && dto.getTemplateId().equals(recordDto.getTemplateId())
                        && dto.getVisualAngle().equals(recordDto.getVisualAngle())
                        && dto.getItemId().equals(recordDto.getItemId())) {

                    if (recordDto.getYear().equals(dto.getYear()) && recordDto.getRecordTotal().longValue() > 0) {
                        dto.setRecordTotal(recordDto.getRecordTotal());
                    }

                }
            }
        }

//        LOGGER.info("tempRecordList : {}", JSON.toJSONString(tempRecordList));

        List<CostDetail> newRecordList = new ArrayList<>();

        for (CostDetailDto recordDto : tempRecordList) {

            if (recordDto.getRecordTotal().longValue() == 0) {

                Long storeId = recordDto.getStoreId();
                Long templateId = recordDto.getTemplateId();
                String visualAngle = recordDto.getVisualAngle();
                Long itemId = recordDto.getItemId();
                Long year = recordDto.getYear();
//                LOGGER.info("storeId : {}", storeId);
//                LOGGER.info("templateId : {}", templateId);
//                LOGGER.info("visualAngle : {}", visualAngle);
//                LOGGER.info("itemId : {}", itemId);
//                LOGGER.info("year : {}", year);

                for (int i = 1; i <= 12; i++) {
                    CostDetail detail = new CostDetail();
                    detail.setAddTime(TimeUtil.curTime());
                    detail.setAddUserId(1L);
                    detail.setIsDel(Cnst.IsStatus.FALSE);
                    detail.setStoreId(recordDto.getStoreId());
                    detail.setTemplateId(recordDto.getTemplateId());
                    detail.setVisualAngle(recordDto.getVisualAngle());
                    detail.setItemId(recordDto.getItemId());
                    detail.setYear(recordDto.getYear());
                    detail.setMonth(Long.valueOf(i));

                    newRecordList.add(detail);
                }

            }
        }

        if (CollectionUtils.isNotEmpty(newRecordList)) {
            LOGGER.info("保存年份数据");
            costDetailMapper.saveBatch(newRecordList);
        }
    }


}
