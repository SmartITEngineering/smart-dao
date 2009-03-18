/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.version.api;

import java.io.InputStream;

/**
 *
 * @author imyousuf
 */
public interface Content {

    /**
     * Retrieves the content of the resource, it can be anything that represents
     * the resource.
     * @return Content of the resource
     * @throws IllegalStateException if isContentLoaded returns false or
     * getContentSize() < 0
     */
    String getContent()
        throws IllegalStateException;

    /**
     * Return the input stream for reading the content.
     * @return Stream representing the content; NULL iff the resource is marked
     * for delete
     */
    InputStream getContentAsStream();

    /**
     * Retrieve the length of the content. It could be used to determine the
     * number of bytes expected from the input stream and if isContentLoaded is
     * true then getContent().length() and its value is same.
     * @return Length/size of the content; should be non-negative
     */
    int getContentSize();

    /**
     * Retrieves whether the content is fully read from the stream or not. If
     * true then it is safe to invoke getContent().
     * @return True if and only if the cotent stream has reached EOF.
     */
    boolean isContentLoaded();

}
