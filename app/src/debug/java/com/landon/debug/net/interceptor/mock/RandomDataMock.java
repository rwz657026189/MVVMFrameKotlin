package com.landon.debug.net.interceptor.mock;

import android.text.TextUtils;

import com.landon.debug.utils.AssetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

/**
 * @Author Ren Wenzhang
 * @Date 2021/7/1/001 12:08
 * @Description 根据class随机产生数据
 */
public class RandomDataMock extends CommMock {
    // 无有效数据list
    private static final String LIST_URL  = "mock-list";
    // 无有效数据object
    private static final String OBJECT_URL  = "mock-object";
    // 实体类参数名
    private static final String PARAMETER_CLASS  = "class";

    private final MockDataProducer mMockDataProducer;

    private final int[] mCountArr = new int[] {0, 3, 6, 16};

    private RandomDataMock(String url) {
        super(url);
        mMockDataProducer = new MockDataProducer();
    }

    public static RandomDataMock getListMock() {
        return new RandomDataMock(LIST_URL);
    }

    public static RandomDataMock getObjectMock() {
        return new RandomDataMock(OBJECT_URL);
    }

    @Override
    public String responseData(Request request) {
        String result = super.responseData(request);
        if (!TextUtils.isEmpty(result)) {
            try {
                String clsName = request.url().queryParameter(PARAMETER_CLASS);
                String jsonStr = AssetUtil.readFile(result);
                if (jsonStr == null) {
                    return result;
                }
                JSONObject jsonObject = new JSONObject(jsonStr);
                if (OBJECT_URL.equals(url)) {
                    JSONObject object = mMockDataProducer.parseObject(clsName);
                    jsonObject.putOpt("data", object);
                } else if(LIST_URL.equals(url)){
                    JSONObject dataObj = new JSONObject();
                    int count = mCountArr[(int) (mCountArr.length * Math.random())];
                    dataObj.putOpt("count", count);
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < count; i++) {
                        jsonArray.put(i, mMockDataProducer.parseObject(clsName));
                    }
                    dataObj.putOpt("list", jsonArray);
                    jsonObject.putOpt("data", dataObj);
                }
                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int getRespType() {
        return InfMock.RESP_MOCK_RANDOM;
    }
}
