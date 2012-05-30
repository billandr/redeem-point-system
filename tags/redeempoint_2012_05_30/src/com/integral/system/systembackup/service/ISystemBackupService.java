package com.integral.system.systembackup.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.integral.system.systembackup.bean.SystemBackupInfo;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * @author  <a href="mailto: swpigris81@126.com">代超</a>
 * @version $Revision$ 
 */
public interface ISystemBackupService {
    /**
     * <p>Discription:[批量新增或修改]</p>
     * @param instance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void saveOrUpdateAll(Collection<SystemBackupInfo> instance);
    /**
     * <p>Discription:[新增或修改]</p>
     * @param instance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void saveOrUpdate(SystemBackupInfo instance);
    /**
     * <p>Discription:[属性查询]</p>
     * @param instance
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public List findByExample(SystemBackupInfo instance);
    /**
     * <p>Discription:[主键查询]</p>
     * @param id
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public SystemBackupInfo findById( java.lang.String id);
    /**
     * <p>Discription:[批量删除]</p>
     * @param persistentInstance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void deleteAll(Collection<SystemBackupInfo> persistentInstance);
    /**
     * <p>Discription:[删除]</p>
     * @param persistentInstance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void delete(SystemBackupInfo persistentInstance);
    /**
     * <p>Discription:[新增]</p>
     * @param transientInstance
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void save(SystemBackupInfo transientInstance);
    /**
     * <p>Discription:[分页查询]</p>
     * @param start
     * @param limit
     * @param paramMap
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public List queryPageBackList(int start, int limit, Map<String, Object> paramMap);
    /**
     * <p>Discription:[分页查询总记录数]</p>
     * @param paramMap
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public int queryPageBackList(Map<String, Object> paramMap);
}
