/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.dao.common.queryparam;

/**
 *
 * @author imyousuf
 */
public interface NoOperandQueryParamater
    extends QueryParameterWithPropertyName<Void>,
            QueryParameterWithOperator<Void> {

    public void init(ParameterType type,
                     String propertyName,
                     OperatorType operatorType);
}
