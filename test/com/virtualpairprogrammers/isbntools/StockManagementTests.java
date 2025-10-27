package com.virtualpairprogrammers.isbntools;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StockManagementTests {

    ExternalISBNDataService testWebService;
    ExternalISBNDataService testDatabaseService;
    StockManager stockManager;

    @Before
    public void setup() {
        testWebService = mock(ExternalISBNDataService.class);
        stockManager = new StockManager();
        stockManager.setWebService(testWebService);
        testDatabaseService = mock(ExternalISBNDataService.class);
        stockManager.setDatabaseService(testDatabaseService);



    }

    @Test
    public void testCanGetACorrectLocatorCode() {
        //creating a stub with mockito
        when(testWebService.lookup(anyString())).thenReturn(new Book("0140177396", "Of Mice And Men", "J. Steinbeck"));
        when(testDatabaseService.lookup(anyString())).thenReturn(null);

        String isbn = "0140177396";
        String locatorCode = stockManager.getLocatorCode(isbn);
        assertEquals("7396J4",locatorCode);
    }

    @Test
    public void databaseIsUsedIfDataIsPresent() {
        //create a mocked object using Mockito
        //mock method creates a dummy class that is an implementation of the interface

        when(testDatabaseService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));

        String isbn = "0140177396";
        String locatorCode = stockManager.getLocatorCode(isbn);

        verify(testDatabaseService).lookup("0140177396");
        verify(testWebService, never()).lookup(anyString());

    }

    @Test
    public void webserviceIsUsedIfDataIsNotPresentInTheDatabase() {
        when(testDatabaseService.lookup("0140177396")).thenReturn(null);
        when(testWebService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));

        String isbn = "0140177396";
        String locatorCode = stockManager.getLocatorCode(isbn);

        verify(testDatabaseService).lookup("0140177396");
        verify(testWebService).lookup("0140177396");
    }
}
