/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.dao.common;

import com.smartitengineering.domain.PersistentDTO;

/**
 *
 * @author imyousuf
 */
public interface CommonWriteDao<Template extends PersistentDTO> {

    public void save(Template... states);

    public void update(Template... states);

    public void delete(Template... states);

}
