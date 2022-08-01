package com.stelmashok.logistics.util.pagination;

public class PageCounter {

    private static PageCounter instance;

    private PageCounter() {
    }

    public static PageCounter getInstance() {
        if (instance == null) {
            instance = new PageCounter();
        }
        return instance;
    }

    public int countNumberOfPages(int numberOfRecords, int recordsPerPage) {
        int numberOfPages = numberOfRecords / recordsPerPage;
        if (numberOfPages % recordsPerPage > 0 || numberOfPages == 0 || numberOfRecords % recordsPerPage != 0) {
            numberOfPages++;
        }
        return numberOfPages;
    }
}
