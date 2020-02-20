public class ThreadJoinTest {

    public static void main(String[] args) throws InterruptedException {
        ThreadJoin threadOne = new ThreadJoin("晓东");
        ThreadJoin threadTwo = new ThreadJoin("小明");
        ThreadJoin threadThree = new ThreadJoin("哈哈");
        System.out.println("---test start---");
        threadOne.start();
        threadTwo.start();
        threadThree.start();
        System.out.println("threadOne:"+threadOne.isAlive());
        System.out.println("threadTwo:"+threadTwo.isAlive());
        System.out.println("threadThree:"+threadThree.isAlive());
        System.out.println("join before");
        threadOne.join();
        System.out.println("join after");
        System.out.println("threadOne:"+threadOne.isAlive());
        System.out.println("threadTwo:"+threadTwo.isAlive());
        System.out.println("threadThree:"+threadThree.isAlive());

//        threadTwo.join();
        System.out.println("---test end---");
    }
}

class ThreadJoin extends Thread {

    public ThreadJoin(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("i:" + i + ", name:" + this.getName());
        }
    }
}
