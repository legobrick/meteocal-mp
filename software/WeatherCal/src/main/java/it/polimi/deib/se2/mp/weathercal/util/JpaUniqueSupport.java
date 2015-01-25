/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import it.polimi.deib.se2.mp.weathercal.gui.JpaUniqueValidator;
import java.io.Serializable;
import static java.lang.String.format;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

/**
 *
 * @author paolo
 */
@Named
@Singleton
public class JpaUniqueSupport {
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final boolean debug = true;

    public boolean isUnique(Serializable entity, String propertyName, Object propertyValue) {
        if (entity == null || propertyValue == null) {
            return true;
        }
        checkJpaEntityPresence(entity);
        return !existsInDatabase(getEntityName(entity), entity, propertyName, propertyValue);
    }

    private boolean existsInDatabase(String entityName, Serializable entity, String propertyName, Object propertyValue) {
        if (isIdSet(entity)) {
            if(debug) Logger.getLogger(JpaUniqueValidator.class.getName()).log(Level.SEVERE, "are you editing, innit?");
            return existsInDatabaseOnOtherObjects(entityName, entity, propertyName, propertyValue);
        } else {
            if(debug) Logger.getLogger(JpaUniqueValidator.class.getName()).log(Level.SEVERE, "it's new!");
            return existsInDatabaseOnAllObjects(entityName, entity, propertyName, propertyValue);
        }
    }

    private boolean existsInDatabaseOnAllObjects(String entityName, Serializable entity, String propertyName, Object propertyValue) {
        return entityManager //
                .createQuery( //
                        format("select count(c) from %s c where c.%s = :propertyValue", entityName, propertyName), Long.class) //
                .setParameter("propertyValue", propertyValue) //
                .getSingleResult() > 0;
    }

    private boolean existsInDatabaseOnOtherObjects(String entityName, Serializable entity, String propertyName, Object propertyValue) {
        return entityManager //
                .createQuery( //
                        format("select count(c) from %s c where c.%s = :propertyValue and %s != :id", //
                                entityName, propertyName, getPkAttributeName(entity.getClass())), Long.class) //
                .setParameter("propertyValue", propertyValue) //
                .setParameter("id", getId(entity)) //
                .getSingleResult() > 0;
    }

    private void checkJpaEntityPresence(Serializable value) {
        if (value.getClass().getAnnotation(Entity.class) == null) {
            throw new IllegalStateException(value.getClass().getSimpleName() + " is not a JPA entity");
        }
    }

    private String getEntityName(Serializable value) {
        if (value.getClass().getAnnotation(Entity.class) != null) {
            return value.getClass().getSimpleName();
        }
        return value.getClass().getAnnotation(Entity.class).name();
    }

    private String getPkAttributeName(Class<?> clazz) {
        String pkAttributeName = getPkAttributeName(clazz, Id.class);
        return pkAttributeName != null ? pkAttributeName : getPkAttributeName(EmbeddedId.class);
    }

    @SuppressWarnings( { "rawtypes", "unchecked" })
    private String getPkAttributeName(Class<?> clazz, Class annotation) {
        for (Method m : clazz.getMethods()) {
            if (m.getClass().isAnnotationPresent(annotation)) {
                return methodToProperty(m);
            }
        }
        for (Field f : clazz.getFields()) {
            if (f.getAnnotation(annotation) != null) {
                return f.getName();
            }
        }
        return null;
    }

    private String methodToProperty(Method m) {
        String value = m.getName();
        if (value.startsWith("get")) {
            int length = "get".length();
            return value.substring(length, length + 1).toLowerCase() + value.substring(length + 1);
        } else if (value.startsWith("is")) {
            int length = "is".length();
            return value.substring(length, length + 1).toLowerCase() + value.substring(length + 1);
        } else {
            return value;
        }
    }
    private boolean isIdSet(Object entity){
        return getId(entity) != null;
    }
    
    private Object getId(Object entity){
        if(debug) Logger.getLogger(JpaUniqueValidator.class.getName()).log(Level.SEVERE, String.format("l'id di %s e` %s", entity.getClass().getCanonicalName(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity)));
        return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }
}