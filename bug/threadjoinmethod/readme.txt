jdk线程join()调用问题
创建一个线程启动后如果调用join()方法，isAlive()值为false
但是如果是多个线程，只有第一个调用join()方法，其它线程isAlive()值有问题，false和true在多次执行后会结果一致，按理来说未调用join()方法的isAlive()值全部为true
