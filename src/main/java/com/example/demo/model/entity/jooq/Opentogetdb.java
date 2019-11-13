/*
 * This file is generated by jOOQ.
 */
package com.example.demo.model.entity.jooq;


import com.example.demo.model.entity.jooq.tables.Order;
import com.example.demo.model.entity.jooq.tables.User;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Opentogetdb extends SchemaImpl {

    private static final long serialVersionUID = -1308780033;

    /**
     * The reference instance of <code>opentogetdb</code>
     */
    public static final Opentogetdb OPENTOGETDB = new Opentogetdb();

    /**
     * 订单表
     */
    public final Order ORDER = com.example.demo.model.entity.jooq.tables.Order.ORDER;

    /**
     * 会员表
     */
    public final User USER = com.example.demo.model.entity.jooq.tables.User.USER;

    /**
     * No further instances allowed
     */
    private Opentogetdb() {
        super("opentogetdb", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Order.ORDER,
            User.USER);
    }
}