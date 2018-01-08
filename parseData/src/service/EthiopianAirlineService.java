package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import beans.LogisticsInfoModel;

/**
 * @ClassName: EthiopianAirlineService
 * @Description: EthiopianAirline 查询接口
 * @author: fanjh
 * @date: 2018年1月4日 下午8:39:31
 */
public class EthiopianAirlineService {

	/** 
	 * EthiopianAirline 查询接口参数名称 
	 */
	private static final String paramName = "awbNum";
	
	/** 
	 * EthiopianAirline 查询接口
	 */
	private static final String requestURL = "https://www.ethiopianairlines.com/corporate/group/cargo/e-cargo/track";
	
	/**
	 * @Title: parseEthiopianAirline
	 * @Description: 埃色俄比亚空运数据处理
	 * @param billNo
	 * @return: List<EthiopianAirlinesModel>
	 * @throws IOException 
	 */
	public static List<LogisticsInfoModel> parseEthiopianAirline(String billNo) {
		Map<String, String> map = new ConcurrentHashMap<>();
		map.put(paramName, billNo);
		
		Document doc = null;
		try {
			doc = Jsoup.connect(requestURL).timeout(10000).data(map).post();
		} catch (IOException e) {
			System.out.println("<--------error! billNo is --------->" + billNo);
		}
		if (null == doc) {
			System.out.println("<------no detail of billNo ------->" + billNo);
			return null;
		}
		Element detailTr = doc.getElementById("detail");
		if (null == detailTr) {
			System.out.println("<------no detail of billNo ------->" + billNo);
			return null;
		}
		Elements trs = detailTr.select("tr:not(:first-child)");
		List<LogisticsInfoModel> models = new ArrayList<LogisticsInfoModel>();
		for (Element tr : trs) {
			LogisticsInfoModel model = new LogisticsInfoModel();
			Elements tds = tr.getElementsByTag("td");
			if (null == tds || tds.isEmpty()) {
				continue;
			}
			StringBuffer sb = new StringBuffer();
			sb.append("状态：" + tds.eq(8).html());
			sb.append("，航班信息：" + tds.eq(0).html() + " " + tds.eq(1).html() + " " + tds.eq(2).html());
			sb.append("，预计到达时间：" + tds.eq(3).html()+ " " + tds.eq(4).html());
			sb.append("，货物信息：" + tds.eq(5).html() + " " +  tds.eq(6).html() + " " + tds.eq(7).html());
			sb.append("，时间：" +  tds.eq(9).html() + " " + tds.eq(10).html());
			model.billNo = billNo;
			model.gmtDate = new Date();
			model.text = sb.toString();
			models.add(model);
		}
		return models;
	}
	
	public static void main(String[] args) throws IOException {
		List<LogisticsInfoModel> parseEthiopianAirlines = parseEthiopianAirline("30908360");
		for (LogisticsInfoModel m: parseEthiopianAirlines) {
			System.out.println(m.toString());
		}
	}
	
}
