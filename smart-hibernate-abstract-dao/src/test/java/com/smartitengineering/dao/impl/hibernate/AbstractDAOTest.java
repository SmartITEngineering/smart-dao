/*
 * This is a common dao with basic CRUD operations and is not limited to any 
 * persistent layer implementation
 * 
 * Copyright (C) 2008  Imran M Yousuf
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.dao.impl.hibernate;

import com.smartitengineering.dao.impl.hibernate.domain.Author;
import com.smartitengineering.dao.impl.hibernate.domain.Book;
import com.smartitengineering.dao.impl.hibernate.domain.Publisher;
import com.smartitengineering.domain.PersistentDTO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author imyousuf
 */
public class AbstractDAOTest
    extends TestCase {

    private static ApplicationContext context;

    public AbstractDAOTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        if (context == null) {
            context = new ClassPathXmlApplicationContext("app-context.xml");
        }
    }

    @Override
    protected void tearDown()
        throws Exception {
        super.tearDown();
    }

    /**
     * Test of createEntity method, of class AbstractDAO.
     */
    public void testCreateEntity() {
        System.out.println("createEntity");
        AbstractDAO<Book> bookInstance = getDaoInstance();
        AbstractDAO<Author> authorInstance = getDaoInstance();
        AbstractDAO<Publisher> publisherInstance = getDaoInstance();
        /**
         * Test null object creation, should throw IllegalArgumentException
         */
        try {
            bookInstance.createEntity((Book) null);
            fail("Should not proceed with saving NULL entity!");
        }
        catch (IllegalArgumentException argumentException) {
        }
        catch (Exception exception) {
            fail(exception.getMessage());
        }
        /**
         * Create proper test data.
         * This set will contain single of everything - author, publisher, book
         */
        Publisher shebaProkashani = getShebaProkashani();
        try {
            publisherInstance.createEntity(shebaProkashani);
            System.out.println(shebaProkashani.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        Author kaziAnowarHossain = getKaziAnowarHossain();
        try {
            authorInstance.createEntity(kaziAnowarHossain);
            System.out.println(kaziAnowarHossain.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        Book book = getAgniSopoth(kaziAnowarHossain, shebaProkashani);
        try {
            bookInstance.createEntity(book);
            System.out.println(book.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        /**
         * Create multiple publications at once.
         * One of the publications should have more than one book
         */
        Publisher oReilly = getOReilly();
        Publisher annoProkash = getAnnoProkash();
        try {
            publisherInstance.createEntity(oReilly, annoProkash);
            System.out.println(oReilly.getId() + ", " + annoProkash.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        /**
         * Create multiple authors at once.
         * There should be at least one book with multiple authors.
         * A single author (set) should have multiple books.
         */
        Author brettMcLaugblin = getBrett();
        Author davidLane = getDavidLane();
        Author hughWilliams = getHughWilliams();
        Author humayunAhmed = getHumayunAhmed();
        try {
            authorInstance.createEntity(brettMcLaugblin, davidLane, humayunAhmed,
                hughWilliams);
            System.out.println(brettMcLaugblin.getId() + ", " +
                davidLane.getId() + ", " + humayunAhmed.getId() + ", " +
                hughWilliams.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        Book kothaoKeoNei = getKothaoKeoNei(annoProkash, humayunAhmed);
        Book agunerPoroshMoni = getAgunerPoroshMoni(annoProkash, humayunAhmed);
        Book webDbApp = getWebDbApp(oReilly, davidLane, hughWilliams);
        Book javaAndXml = getJavaAndXml(oReilly, brettMcLaugblin);
        try {
            bookInstance.createEntity(kothaoKeoNei, agunerPoroshMoni, webDbApp,
                javaAndXml);
            System.out.println(kothaoKeoNei.getId() + ", " +
                agunerPoroshMoni.getId() + ", " + webDbApp.getId() + ", " +
                javaAndXml.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of updateEntity method, of class AbstractDAO.
     */
    public void testUpdateEntity() {
        System.out.println("updateEntity");
    }

    /**
     * Test of deleteEntity method, of class AbstractDAO.
     */
    public void testDeleteEntity() {
        System.out.println("deleteEntity");
    }

    /**
     * Test of readSingle method, of class AbstractDAO.
     */
    public void testReadSingle_Class_Hashtable() {
        System.out.println("readSingle");
    }

    /**
     * Test of readOther method, of class AbstractDAO.
     */
    public void testReadOther_Class_Hashtable() {
        System.out.println("readOther");
    }

    /**
     * Test of readOtherList method, of class AbstractDAO.
     */
    public void testReadOtherList_Class_Hashtable() {
        System.out.println("readOtherList");
    }

    /**
     * Test of readList method, of class AbstractDAO.
     */
    public void testReadList_Class_Hashtable() {
        System.out.println("readList");
    }

    /**
     * Test of readSingle method, of class AbstractDAO.
     */
    public void testReadSingle_Class_List() {
        System.out.println("readSingle");
    }

    /**
     * Test of readOther method, of class AbstractDAO.
     */
    public void testReadOther_Class_List() {
        System.out.println("readOther");
    }

    /**
     * Test of readOtherList method, of class AbstractDAO.
     */
    public void testReadOtherList_Class_List() {
        System.out.println("readOtherList");
    }

    /**
     * Test of readList method, of class AbstractDAO.
     */
    public void testReadList_Class_List() {
        System.out.println("readList");
    }

    /**
     * Test of readSingle method, of class AbstractDAO.
     */
    public void testReadSingle_Class_QueryParameterArr() {
        System.out.println("readSingle");
    }

    /**
     * Test of readOther method, of class AbstractDAO.
     */
    public void testReadOther_Class_QueryParameterArr() {
        System.out.println("readOther");
    }

    /**
     * Test of readOtherList method, of class AbstractDAO.
     */
    public void testReadOtherList_Class_QueryParameterArr() {
        System.out.println("readOtherList");
    }

    /**
     * Test of readList method, of class AbstractDAO.
     */
    public void testReadList_Class_QueryParameterArr() {
        System.out.println("readList");
    }

    private Book getAgniSopoth(Author author,
                               Publisher publisher) {
        final String bookName = "Agni Sopoth";
        final Date publishDate = new Date();
        final String isbn = "123ABC";
        return getBook(publisher, bookName, publishDate, isbn, author);
    }

    private Book getAgunerPoroshMoni(Publisher annoProkash,
                                     Author humayunAhmed) {
        return getBook(annoProkash, "Aguner Poroshmoni", new Date(), "222VFEE3",
            humayunAhmed);
    }

    private Publisher getAnnoProkash() {
        return getPublisher(new Date(), 50, "Anno Prokash");
    }

    private Author getBrett() {
        return getAuthor("Brett McLaugblin", new Date());
    }

    private Author getDavidLane() {
        return getAuthor("David Lane", new Date());
    }

    private Author getHughWilliams() {
        return getAuthor("Hugh E. Williams", new Date());
    }

    private Author getHumayunAhmed() {
        return getAuthor("Humayun Ahmed", new Date());
    }

    private Book getJavaAndXml(Publisher oReilly,
                               Author brettMcLaugblin) {
        return getBook(oReilly, "Java & XML", new Date(), "555UIP66",
            brettMcLaugblin);
    }

    private Author getKaziAnowarHossain() {
        final String name = "Kazi Anowar Hossain";
        final Date birthDate = new Date();
        return getAuthor(name, birthDate);
    }

    private Book getKothaoKeoNei(Publisher annoProkash,
                                 Author humayunAhmed) {
        return getBook(annoProkash, "Kothao Keo Nei", new Date(), "11134BCE",
            humayunAhmed);
    }

    private Publisher getOReilly() {
        return getPublisher(new Date(), 110, "O\'Reilly");
    }

    private Publisher getShebaProkashani() {
        final String name = "Sheba Prokashoni";
        final Date establishDate = new Date();
        final int numOfEmployees = 100;
        return getPublisher(establishDate, numOfEmployees, name);
    }

    private Publisher getPublisher(final Date establishDate,
                                   final int numOfEmployees,
                                   final String name) {
        Publisher publisher =
            new Publisher();
        publisher.setEstablishedDate(establishDate);
        publisher.setNumOfEmployees(numOfEmployees);
        publisher.setName(name);
        return publisher;
    }

    private Author getAuthor(final String name,
                             final Date birthDate) {
        Author author =
            new Author();
        author.setName(name);
        author.setBirthDate(birthDate);
        return author;
    }

    private Book getBook(final Publisher publisher,
                         final String bookName,
                         final Date publishDate,
                         final String isbn,
                         final Author... authors) {
        Book book = new Book();
        Set<Author> authorSet =
            new HashSet<Author>();
        for (Author author : authors) {
            authorSet.add(author);
        }
        book.setAuthors(authorSet);
        book.setPublisher(publisher);
        book.setName(bookName);
        book.setPublishDate(publishDate);
        book.setIsbn(isbn);
        return book;
    }

    private <T extends PersistentDTO<T>> AbstractDAO<T> getDaoInstance()
        throws BeansException {
        AbstractDAO<T> instance =
            (AbstractDAO<T>) context.getBean("testDao");
        assertNotNull(instance);
        return instance;
    }

    private Book getWebDbApp(Publisher oReilly,
                             Author davidLane,
                             Author hughWilliams) {
        return getBook(oReilly, "Web Database Applications", new Date(),
            "444ERT6", davidLane, hughWilliams);
    }
}
