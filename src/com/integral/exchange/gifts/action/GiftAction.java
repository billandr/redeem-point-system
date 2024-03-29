package com.integral.exchange.gifts.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.integral.common.action.BaseAction;
import com.integral.exchange.gifts.bean.GiftInfo;
import com.integral.exchange.gifts.service.IGiftService;
import com.integral.util.RequestUtil;
import com.integral.util.Tools;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * @author  <a href="mailto: xxx@neusoft.com">作者中文名</a>
 * @version $Revision$ 
 */
public class GiftAction extends BaseAction implements ServletRequestAware, ServletResponseAware {

    private HttpServletRequest request;
    private HttpServletResponse response;
    
    private IGiftService giftService;
    
    /** 文件上传  */
    private String title;
    private File[] giftImage;
    private String[] giftImageContentType;
    private String[] giftImageFileName;
    private String savePath;

    /** 事务处理 */
    private DataSourceTransactionManager transactionManager;
    
    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return String title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return File[] giftImage.
     */
    public File[] getGiftImage() {
        return giftImage;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param giftImage The giftImage to set.
     */
    public void setGiftImage(File[] giftImage) {
        this.giftImage = giftImage;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return String[] giftImageContentType.
     */
    public String[] getGiftImageContentType() {
        return giftImageContentType;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param giftImageContentType The giftImageContentType to set.
     */
    public void setGiftImageContentType(String[] giftImageContentType) {
        this.giftImageContentType = giftImageContentType;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return String[] giftImageFileName.
     */
    public String[] getGiftImageFileName() {
        return giftImageFileName;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param giftImageFileName The giftImageFileName to set.
     */
    public void setGiftImageFileName(String[] giftImageFileName) {
        this.giftImageFileName = giftImageFileName;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return String savePath.
     */
    public String getSavePath() {
        return ServletActionContext.getRequest().getRealPath(savePath);
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param savePath The savePath to set.
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return IGiftService giftService.
     */
    public IGiftService getGiftService() {
        return giftService;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param giftService The giftService to set.
     */
    public void setGiftService(IGiftService giftService) {
        this.giftService = giftService;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @return DataSourceTransactionManager transactionManager.
     */
    public DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param transactionManager The transactionManager to set.
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param request The request to set.
     */
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param response The response to set.
     */
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
    /**
     * <p>Discription:[礼品管理页面]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String begin(){
        return SUCCESS;
    }
    
    /**
     * 所有客户能看到的礼品列表(jQuery列表展示)
     * <p>Discription:[方法功能中文描述]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String giftManageList(){
        int start = NumberUtils.toInt(request.getParameter("start"), 0);
        int limit = NumberUtils.toInt(request.getParameter("limit"), 50);
        int page = NumberUtils.toInt(request.getParameter("page"), 0);
        
        List list = this.giftService.findByPage(start+limit*(page-1), limit+limit*(page-1));
        long giftSize = this.giftService.findAllGiftSize();
        PrintWriter out = null;
        try {
            out = super.getPrintWriter(request, response);
            //true:不换行，忽略null
            JsonFormat jf = new JsonFormat(true);
            //设置Unicode编码
            jf.setAutoUnicode(true);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", true);
            map.put("totalCount", list.size());
            BigDecimal recordSize = new BigDecimal(""+giftSize);
            BigDecimal pageCount = new BigDecimal("0");
            pageCount = recordSize.divide(new BigDecimal(""+limit), BigDecimal.ROUND_CEILING);
            map.put("pageCount", pageCount.intValue());
            map.put("giftList", list);
            out.print(Json.toJson(map, jf));
            //out.print("{\"success\":\"true\",\"totalCount\":\"34\"}");
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * 供应商能看到的礼品列表（EXT后台管理）
     * <p>Discription:[方法功能中文描述]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String giftList(){
        int start = NumberUtils.toInt(request.getParameter("start"), 0);
        int limit = NumberUtils.toInt(request.getParameter("limit"), 50);
        String supplierId = request.getParameter("supplierId");
        List list = this.giftService.findByPageWithSupplier(supplierId, start, limit);
        long size = this.giftService.findAllSupplierGiftSize(supplierId);
        PrintWriter out = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            out = super.getPrintWriter(request, response);
            JsonFormat jf = new JsonFormat();
            jf.setAutoUnicode(true);
            map.put("success", true);
            map.put("totalCount", size);
            map.put("giftList", list);
            out.print(Json.toJson(map, jf));
        }catch(Exception e){
            map.put("success", false);
            out.print(Json.toJson(map));
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
        return null;
    }
    /**
     * 新增礼品信息
     * <p>Discription:[方法功能中文描述]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String addGift(){
        File[] files = getGiftImage();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        PrintWriter out = null;
        Map requestMap = RequestUtil.getRequestMap(request);
        Map<String, Object> map = new HashMap<String, Object>();
        
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        try{
            out = super.getPrintWriter(request, response, "UTF-8", "text/html");
            
            String imagePath = "";
            if(files != null){
                for(int i=0; i< files.length; i++){
                    String imageName = getGiftImageFileName()[i];
                    String imageExt = ".jpg";
                    String uuid = Tools.getUUID();
                    if(imageName != null && !"".equals(imageName)){
                        imageExt = imageName.substring(imageName.lastIndexOf("."));
                    }
                    //以服务器的文件保存地址和原文件名建立上传文件输出流
                    fos = new FileOutputStream(getSavePath() + "\\" + uuid+imageExt);
                    fis = new FileInputStream(files[i]);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = fis.read(buffer)) > 0){
                        fos.write(buffer , 0 , len);
                    }
                    
                    imagePath = savePath + "/" + uuid+imageExt;
                }
            }
            
            GiftInfo gift = new GiftInfo();
            BeanUtils.populate(gift, requestMap);
            gift.setGiftImage(imagePath);
            if(gift.getGiftId() == null || "".equals(gift.getGiftId())){
                gift.setGiftId(null);
            }
            this.giftService.saveOrUpdate(gift);
            map.put("success", true);
            out.print(Json.toJson(map));
        }catch(Exception e){
            status.setRollbackOnly();
            map.put("success", false);
            out.print(Json.toJson(map));
        }finally{
            this.transactionManager.commit(status);
            if(out!=null){
                out.flush();
                out.close();
            }
            try{
                if(fos != null){
                    fos.close();
                }
                if(fis != null){
                    fis.close();
                }
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 修改礼品信息
     * <p>Discription:[方法功能中文描述]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String editGift(){
        File[] files = getGiftImage();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        PrintWriter out = null;
        Map requestMap = RequestUtil.getRequestMap(request);
        Map<String, Object> map = new HashMap<String, Object>();
        
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        try{
            out = super.getPrintWriter(request, response, "UTF-8", "text/html");
            
            String imagePath = "";
            GiftInfo gift = this.giftService.findById(String.valueOf(requestMap.get("giftId")));
            if(files != null){
                for(int i=0; i< files.length; i++){
                    String imageName = getGiftImageFileName()[i];
                    String imageExt = ".jpg";
                    String uuid = Tools.getUUID();
                    if(imageName != null && !"".equals(imageName)){
                        imageExt = imageName.substring(imageName.lastIndexOf("."));
                    }
                    //以服务器的文件保存地址和原文件名建立上传文件输出流
                    fos = new FileOutputStream(getSavePath() + "\\" + uuid+imageExt);
                    fis = new FileInputStream(files[i]);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = fis.read(buffer)) > 0){
                        fos.write(buffer , 0 , len);
                    }
                    
                    imagePath = savePath + "/" + uuid+imageExt;
                }
            }
            //应该将该礼品之前的图片删掉，避免图片越来越多
            //如果修改该礼品信息时，上传了新的图片，则应该删除旧图片，否则不删除。
            if(files != null){
                if(gift == null){
                    gift = new GiftInfo();
                }else{
                    String giftImg = gift.getGiftImage();
                    if(giftImg != null && !"".equals(giftImg.trim())){
                        File f = new File(savePath + "/" +giftImg);
                        if(f.exists()){
                            f.delete();
                        }
                    }
                }
            }else{
                //设置修改后的图片路径为修改前的图片路径
                imagePath = gift.getGiftImage();
            }
            BeanUtils.populate(gift, requestMap);
            gift.setGiftImage(imagePath);
            if(gift.getGiftId() == null || "".equals(gift.getGiftId())){
                gift.setGiftId(null);
            }
            this.giftService.saveOrUpdate(gift);
            map.put("success", true);
            out.print(Json.toJson(map));
        }catch(Exception e){
            status.setRollbackOnly();
            map.put("success", false);
            out.print(Json.toJson(map));
            e.printStackTrace();
        }finally{
            this.transactionManager.commit(status);
            if(out!=null){
                out.flush();
                out.close();
            }
            try{
                if(fos != null){
                    fos.close();
                }
                if(fis != null){
                    fis.close();
                }
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 删除礼品信息
     * <p>Discription:[方法功能中文描述]</p>
     * @return
     * @author:[代超]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String deleteGift(){
        String giftIds = request.getParameter("gifts");
        
        // 定义TransactionDefinition并设置好事务的隔离级别和传播方式。
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 代价最大、可靠性最高的隔离级别，所有的事务都是按顺序一个接一个地执行
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(definition);
        
        PrintWriter out = null;
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try{
            out = super.getPrintWriter(request, response);
            if(giftIds == null || "".equals(giftIds.trim())){
                resultMap.put("success", false);
                resultMap.put("msg", "您没有选择礼品信息！");
            }else{
                this.giftService.deleteAllGifts(giftIds.split(","));
                resultMap.put("success", true);
                resultMap.put("msg", "您选择的礼品信息已成功删除！");
            }
        }catch(Exception e){
            status.setRollbackOnly();
            resultMap.put("success", false);
            resultMap.put("msg", "删除礼品信息过程中出现异常！");
        }finally{
            this.transactionManager.commit(status);
            out.print(Json.toJson(resultMap));
            if(out != null){
                out.flush();
                out.close();
            }
        }
        return null;
    }
}
