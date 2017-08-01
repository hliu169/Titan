
package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

/**
 * Servlet implementation class ItemHistory
 */
@WebServlet("/history")
public class ItemHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = DBConnectionFactory.getDBConnection();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ger user_id from request
//		String userId = request.getParameter("user_id");
//		try {
//			if (userId != null) {
//				Set<Item> histories = conn.getFavoriteItems(userId);
//
//				JSONArray array = new JSONArray();
//				for (Item history : histories) {
//					array.put(history.toJSONObject());
//				}
//				// send reseults to response to display
//				RpcHelper.writeJsonArray(response, array);
//			} else {
//				RpcHelper.writeJsonObject(response, new JSONObject().put("status", "Invalid parameter"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		String userId = request.getParameter("user_id");
		   Set<Item> items = conn.getFavoriteItems(userId);
		   JSONArray array = new JSONArray();
		   for (Item item : items) {
		     JSONObject obj = item.toJSONObject();
		     try {
		       obj.append("favorite", true); // why do we need this line? To identify that this is "favorites"?
		     } catch (JSONException e) {
		       e.printStackTrace();
		     }
		     array.put(obj);
		   }
		   RpcHelper.writeJsonArray(response, array);


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			JSONArray array = (JSONArray) input.get("favorite");

			List<String> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				String itemId = (String) array.get(i);
				histories.add(itemId);
			}
			conn.setFavoriteItems(userId, histories);

			// coded by myself for unset favorites
			// should use DELETE method, and keep favorites in user input.
			// Doens't understand REST deep enough

			// array = (JSONArray) input.get("unfavorite");
			// histories.clear();
			//
			// for (int i = 0; i < array.length(); i++) {
			// String itemId = (String) array.get(i);
			// histories.add(itemId);
			// }
			//
			// conn.unsetFavoriteItems(userId, histories);

			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			JSONArray array = (JSONArray) input.get("favorite");

			List<String> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				String itemId = (String) array.get(i);
				histories.add(itemId);
			}
			conn.unsetFavoriteItems(userId, histories);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
