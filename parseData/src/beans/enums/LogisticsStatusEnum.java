package beans.enums;

/**
 * @ClassName: LogisticsStatusEnum
 * @Description: 物流航运状态
 * @author: fanjh
 * @date: 2018年1月8日 下午7:06:35
 */
public enum LogisticsStatusEnum {

	DEPARTED("已出发", "1"),
	DELIVERED("已送达", "2");
	
	/** 
	 * 状态名称 
	 */
	private final String name;
	
	/** 
	 * 状态值
	 */
	private final String code;
	
	private LogisticsStatusEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
}
