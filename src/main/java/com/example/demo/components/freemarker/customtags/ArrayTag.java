package com.example.demo.components.freemarker.customtags;

import freemarker.core._DelayedJQuote;
import freemarker.core._TemplateModelException;
import freemarker.template.*;

import java.util.*;

/**
 * @author chen_bq
 * @description 实现标签组的基类
 * @create: 2019-03-21 15:47
 **/
public class ArrayTag extends WrappingTemplateModel implements TemplateHashModelEx2 {

    private Map map;

    /**
     * 键值对迭代器
     * @return
     * @throws TemplateModelException
     */
    @Override
    public KeyValuePairIterator keyValuePairIterator() throws TemplateModelException {
        return new MapKeyValuePairIterator(this.map, this.getObjectWrapper());
    }

    /**
     * 单纯的返回标签组长度
     * @return
     * @throws TemplateModelException
     */
    @Override
    public int size() throws TemplateModelException {
        return this.map.size();
    }

    /**
     * 模板锁
     * @return
     * @throws TemplateModelException
     */
    @Override
    public TemplateCollectionModel keys() throws TemplateModelException {
        ArrayTag var1 = ArrayTag.this;
        synchronized(ArrayTag.this) {
            return ArrayTag.this.keys();
        }
    }

    /**
     * 模板值锁
     * @return
     * @throws TemplateModelException
     */
    @Override
    public TemplateCollectionModel values() throws TemplateModelException {
        ArrayTag var1 = ArrayTag.this;
        synchronized(ArrayTag.this) {
            return ArrayTag.this.values();
        }
    }

    /**
     * @Author chen_bq
     * @Description 获得标签组对象key值，通过“.”获得自定义的标签库标签
     * @Date 2019/3/21 16:36
     * @Param [key]
     * @return freemarker.template.TemplateModel
     **/
    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        Object result;
        try {
            result = this.map.get(key);
        } catch (ClassCastException var7) {
            throw new _TemplateModelException(var7, new Object[]{"ClassCastException while getting Map entry with String key ", new _DelayedJQuote(key)});
        } catch (NullPointerException var8) {
            throw new _TemplateModelException(var8, new Object[]{"NullPointerException while getting Map entry with String key ", new _DelayedJQuote(key)});
        }
        //测试直接返回值
        return this.wrap(result);
    }

    @Override
    public boolean isEmpty() throws TemplateModelException {
        return this.map == null || this.map.isEmpty();
    }


    /** @deprecated */
    @Deprecated
    public ArrayTag() {
        this((ObjectWrapper)null);
    }


    /** @deprecated */
    @Deprecated
    public ArrayTag(Map map) {
        this(map, (ObjectWrapper)null);
    }

    public ArrayTag(ObjectWrapper wrapper) {
        super(wrapper);
        this.map = new HashMap();
    }

    public ArrayTag(Map map, ObjectWrapper wrapper) {
        super(wrapper);

        Map mapCopy;
        try {
            mapCopy = this.copyMap(map);
        } catch (ConcurrentModificationException var9) {
            try {
                Thread.sleep(5L);
            } catch (InterruptedException var8) {
                ;
            }

            synchronized(map) {
                mapCopy = this.copyMap(map);
            }
        }

        this.map = mapCopy;
    }

    protected Map copyMap(Map map) {
        if (map instanceof HashMap) {
            return (Map)((HashMap)map).clone();
        } else if (map instanceof SortedMap) {
            return (Map)(map instanceof TreeMap ? (Map)((TreeMap)map).clone() : new TreeMap((SortedMap)map));
        } else {
            return new HashMap(map);
        }
    }

    public void put(String key, Object value) {
        this.map.put(key, value);
    }

    public void put(String key, boolean b) {
        this.put(key, b ? TemplateBooleanModel.TRUE : TemplateBooleanModel.FALSE);
    }

}
