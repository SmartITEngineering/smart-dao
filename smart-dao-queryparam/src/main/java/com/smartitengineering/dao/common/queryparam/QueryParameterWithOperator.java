/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.dao.common.queryparam;

/**
 *
 * @author imyousuf
 */
public interface QueryParameterWithOperator<Template extends Object> extends QueryParameter<Template>{
    public OperatorType getOperatorType();
}
