package com.rakibulh.main;

import java.util.concurrent.TimeUnit;

public class SharedObject {
    private ThreadLocal<Integer> number = new ThreadLocal<>();

    public Integer getNumber() {
        return number.get();
    }
    public void setNumber(Integer number) {
        this.number.set(number);
    }

    public static void main(String[] args) {
        // sharedObject for all threads, including main thread
        SharedObject sharedObject = new SharedObject();
        // first, set a random number from main thread
        setARandomNumber(sharedObject);

        // secondly, set another random number from thread1
        Thread thread1 = new Thread(() -> {
           setARandomNumber(sharedObject);
        });
        // thirdly, set another random number from thread1
        Thread thread2 = new Thread(() -> {
           setARandomNumber(sharedObject);
        });
        // spin two threads at the same time
        thread1.start();
        thread2.start();

        try{
            // wait for the treads to finish their works
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }
        // print and see if the above threads changes effected main threads value ? "NO"
        System.out.println("Thread: "+Thread.currentThread().getName());
        System.out.println("Value: "+sharedObject.getNumber());
    }

    // whenever any threads call this method, it will change the objects value and print those
    private static void setARandomNumber(SharedObject sharedObject) {
        sharedObject.setNumber((int) (Math.random()*100));
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }

        System.out.println("Thread: "+Thread.currentThread().getName());
        System.out.println("Value: "+sharedObject.getNumber());
    }
}
