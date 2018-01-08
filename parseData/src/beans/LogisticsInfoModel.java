package beans;

import java.util.Date;

/**
 * @ClassName: LogisticsInfoModel
 * @Description: 物流信息数据模型
 * @author: fanjh
 * @date: 2018年1月5日 下午3:46:14
 */
public class LogisticsInfoModel {

	/** 
	 * 账单号 
	 */
	public String billNo;
	
	/**
	 *  修改时间
	 */
	public Date gmtDate;
	
	/** 
	 * 文本
	 */
	public String text;

	@Override
	public String toString() {
		return "LogisticsInfoModel [billNo=" + billNo + ", gmtDate=" + gmtDate + ", text=" + text + "]";
	}
}
