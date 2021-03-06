package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import beans.CargoTrackingRequestSO;
import beans.LogisticsInfoModel;
import beans.enums.LogisticsStatusEnum;

/**
 * @ClassName: QatarCargoService
 * @Description: 卡塔尔数据接口
 * @author: fanjh
 * @date: 2018年1月5日 下午4:13:51
 */
public class QatarCargoService {
	
	/** 
	 * QatarCargoService 查询接口
	 */
	private static final String requestURL = "http://www.qrcargo.com/doTrackShipmentsAction";
	
	/**
	 *  物流状态：已出发 
	 */
	private static final String STATUS_DEPARTED = "DEP";
	
	/**
	 *  物流状态：已送达 
	 */
	private static final String STATUS_DELIVERED= "DLV";
	
	public static List<LogisticsInfoModel> parseQatarCargo(String billNo) {
		
		if (null == billNo || StringUtil.isBlank(billNo)) {
			System.out.println("<--------billNo is Null--------->");
			return null;
		}
		
		// 设置请求头 该查询接口有下列要求
		Map<String, String> headers = new ConcurrentHashMap<>();
		headers.put("Content-Type", "application/json; charset=UTF-8");
		headers.put("Referer", "http://www.qrcargo.com/trackshipment");
		
		/**组装参数 格式为 {"cargoTrackingRequestSOs":
		 * [{"documentType":"MAWB","documentPrefix":"157","documentNumber":"33305300"}]} 
		 */
		Gson gson = new Gson();
		List<CargoTrackingRequestSO> cargoTrackingRequestSOs = new ArrayList<CargoTrackingRequestSO>();
		CargoTrackingRequestSO cargoTrackingRequestSO = new CargoTrackingRequestSO(billNo);
		cargoTrackingRequestSOs.add(cargoTrackingRequestSO);
		Map<String, List<CargoTrackingRequestSO>> map = 
				new ConcurrentHashMap<String, List<CargoTrackingRequestSO>>();
		map.put("cargoTrackingRequestSOs", cargoTrackingRequestSOs);
		
		Document doc = null;
		try {
			doc= Jsoup.connect(requestURL).timeout(10000).requestBody(gson.toJson(map))
					.headers(headers).post();
		} catch (IOException e) {
			System.out.println("<--------error! billNo is --------->" + billNo);
		}
		if (null == doc) {
			System.out.println("<------no detail of billNo ------->" + billNo);
			return null;
		}
		// 获取页面内容
		Elements table = doc.getElementsByClass("responsiveDatagrid-2");
		if (null == table) {
			System.out.println("<------no detail of billNo ------->" + billNo);
			return null;
		}
		Elements trs = table.select("tr:not(:first-child)");
		if (null == trs || trs.isEmpty()) {
			System.out.println("<------no detail of billNo ------->" + billNo);
			return null;
		}
		Integer departedIndex = null;
		int i = 0;
		boolean deliveredFlag = true;
		List<LogisticsInfoModel> models = new ArrayList<LogisticsInfoModel>();
		for (Element tr : trs) {
			LogisticsInfoModel model = new LogisticsInfoModel();
			i++;
			Elements tds = tr.getElementsByTag("td");
			if (null == tds || tds.isEmpty()) {
				continue;
			}
			String status = tds.eq(0).html();
			if (StringUtils.equals(status, STATUS_DEPARTED)) {
				departedIndex = i - 1;
			} else if (deliveredFlag &&StringUtils.equals(status, STATUS_DELIVERED)) {
				model.status = LogisticsStatusEnum.DELIVERED.getCode();
			}
			StringBuffer sb = new StringBuffer();
			sb.append("状态：" + status);
			sb.append("，所在机场：" + tds.eq(1).html());
			sb.append("，日期：" + tds.eq(2).html());
			sb.append("，详情：" + tds.eq(3).html());
			model.billNo = billNo;
			model.gmtDate = new Date();
			model.text = sb.toString();
			models.add(model);
		}
		if (null != departedIndex) {
			models.get(departedIndex).status = LogisticsStatusEnum.DEPARTED.getCode();
			models.set(departedIndex, models.get(departedIndex));
		}
		Collections.reverse(models);
		return models;
	}
	
	public static void main(String[] args) {
		List<LogisticsInfoModel> parseEthiopianAirlines = parseQatarCargo("33305300");
		for (LogisticsInfoModel m: parseEthiopianAirlines) {
			System.out.println(m.toString());
		}
	}
}
