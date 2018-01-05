package beans;

/**
 * @ClassName: CargoTrackingRequestSO
 * @Description: 卡塔尔参数类
 * @author: fanjh
 * @date: 2018年1月5日 下午4:25:16
 */
public class CargoTrackingRequestSO {
	
	public String documentType = "MAWB";
	
	public String documentPrefix = "157";
	
	public String documentNumber;

	public CargoTrackingRequestSO(String documentNumber) {
		super();
		this.documentNumber = documentNumber;
	}
}
