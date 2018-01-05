package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import beans.CargoTrackingRequestSO;
import beans.LogisticsInfoModel;

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
	
	public static List<LogisticsInfoModel> parseQatarCargo(String billNo) {
		Map<String, String> map = new ConcurrentHashMap<>();
//		map.put(paramName, billNo);
		CargoTrackingRequestSO cargoTrackingRequestSO = new CargoTrackingRequestSO("33305300");
		List<CargoTrackingRequestSO> cargoTrackingRequestSOs = new ArrayList<CargoTrackingRequestSO>();;
		cargoTrackingRequestSOs.add(cargoTrackingRequestSO);
		map.put("cargoTrackingRequestSOs", new Gson().toJson(cargoTrackingRequestSOs));
		String jsonPayload = "cargoTrackingRequestSOs"
				+ ":[{\"documentType\": \"MAWB\", \"documentPrefix\": \"157\", \"documentNumber\": \"33305300\"}]";
		Document doc = null;
		try {
//			doc = Jsoup.connect(requestURL).header("Content-Type", "application/json;charset=UTF-8").ignoreContentType(true)
//					.data("payload",new Gson().toJson(cargoTrackingRequestSOs)).post();
            String response= Jsoup.connect(requestURL).timeout(60000).ignoreContentType(true)
            		.method(Connection.Method.POST)
                    .data("payload",jsonPayload.toString())
                    .execute()
                    .body();
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == doc) {
			return null;
		}
		Element detailTr = doc.getElementById("detail");
		if (null == detailTr) {
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
	public static void main(String[] args) throws HttpException, IOException {
	    PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
		String jsonPayload = "{cargoTrackingRequestSOs"
				+ ":[{\"documentType\": \"MAWB\", \"documentPrefix\": \"157\", \"documentNumber\": \"33305300\"}]}";
		  URL realUrl = new URL(requestURL);
          // 打开和URL之间的连接
          URLConnection conn = realUrl.openConnection();
          // 设置通用的请求属性\
          conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
          conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
          conn.setRequestProperty("accept", "*/*");
          conn.setRequestProperty("Origin", "http://www.qrcargo.com");
          conn.setRequestProperty("connection", "Keep-Alive");
          conn.setRequestProperty("payload", jsonPayload);
          conn.setRequestProperty("user-agent",
                  "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
          // 发送POST请求必须设置如下两行
          conn.setDoOutput(true);
          conn.setDoInput(true);
          // 获取URLConnection对象对应的输出流
          out = new PrintWriter(conn.getOutputStream());
          // 发送请求参数
          // flush输出流的缓冲
          out.flush();
          // 定义BufferedReader输入流来读取URL的响应
          in = new BufferedReader(
                  new InputStreamReader(conn.getInputStream()));
          String line;
          while ((line = in.readLine()) != null) {
              result += line;
          }
          System.out.println(result);
	}
}
