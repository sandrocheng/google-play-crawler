package com.akdeniz.googleplaycrawler.cli.statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.akdeniz.googleplaycrawler.GooglePlay.AppDetails;
import com.akdeniz.googleplaycrawler.GooglePlay.BrowseLink;
import com.akdeniz.googleplaycrawler.GooglePlay.BrowseResponse;
import com.akdeniz.googleplaycrawler.GooglePlay.DocV2;
import com.akdeniz.googleplaycrawler.GooglePlay.ListResponse;
import com.akdeniz.googleplaycrawler.GooglePlayAPI;
import com.akdeniz.googleplaycrawler.cli.model.AppInfo;
import com.akdeniz.googleplaycrawler.cli.model.CategoryObj;
import com.akdeniz.googleplaycrawler.cli.report.Out;

/**
 * 
 * @author sandro
 *
 */
public class DataHandle {

	private GooglePlayAPI service;
	
	private List<AppInfo> appList = new ArrayList<AppInfo>();
	
	public DataHandle(GooglePlayAPI service){
		this.service = service;
	}
	
	public void startDataHandle()throws Exception{
		List<CategoryObj> mainCatList = fetchMainCat();
		Thread.sleep(1000);
		Map<CategoryObj,List<CategoryObj>> categoreis = fetchAllSubCat(mainCatList);
		Thread.sleep(4000);
		fetchAllAppInfo(categoreis);
	}
	
	private void fetchAllAppInfo(Map<CategoryObj,List<CategoryObj>> categoreis) throws Exception{
		Out.print("---start fetch app info");
		long time = System.currentTimeMillis();
		appList.clear();
		Iterator<CategoryObj> iterator =categoreis.keySet().iterator();
		while(iterator.hasNext()){
			CategoryObj key = iterator.next();
			List<CategoryObj> subCatList = categoreis.get(key);
			Out.print("------ fetch category " + key.getName());
			for(CategoryObj subCat : subCatList){
				fetchAppInfo(key,subCat);
			}
			Out.print("------ fetch category " + key.getName() + " success!");
		}
		Out.print("---fetch app info success! fetch " + appList.size() + " apps, use : " + (System.currentTimeMillis() - time)/1000 + "sec");
	}
	
	private void fetchAppInfo(CategoryObj mainCat, CategoryObj subCat)
			throws Exception {
		Out.print("------ fetch category " + mainCat.getName() + "->" + subCat.getName());
		int start = 0;
		int num = 50;
		int count = 0;
		int errortime = 0;
		while (true) {
			ListResponse listResponse = null;
			try {
				listResponse = service.list(mainCat.getId(),
						subCat.getId(), start, num);
				errortime = 0;
			} catch (Exception e) {
				errortime ++;
				if(errortime >2){
					System.err.println("*** get app info response error 2 times,has to return ");
					return;
				}
				System.err.println("*** get app info response error : " + e.toString() + " ,sleep 10 sec do it again!");
				Thread.sleep(10000);
				continue;
			}
			if (isChildEmpty(listResponse)) {
				Out.print("------ fetch category " + mainCat.getName() + "-" + subCat.getName() + " success total : " + count);
				return;
			}else{
				for (DocV2 child : listResponse.getDoc(0).getChildList()) {
					String title = child.getTitle();
					AppDetails appDetails = child.getDetails().getAppDetails();
					String packageName = appDetails.getPackageName();
					String creator = child.getCreator();
					String price = child.getOffer(0).getFormattedAmount();
					String installationSize = String.valueOf(appDetails
							.getInstallationSize());
					String numberOfDownloads = appDetails.getNumDownloads();
					AppInfo ai = new AppInfo(title, packageName, creator,
							price, installationSize, numberOfDownloads);
					Out.print(ai.toString());
					appList.add(ai);
					count++;
				}
				start += num;
			}
			Thread.sleep(3000);
		}
	}
	
	private boolean isChildEmpty(ListResponse listResponse){
		boolean result = true;
		try {
			result = listResponse.getDoc(0).getChildList() == null
					|| listResponse.getDoc(0).getChildList().isEmpty();
		} catch (Exception e) {
			System.err.println(">>>>>>>>> isChildEmpty error : " + e.toString());
		}
		
		return result;
	}

	private List<CategoryObj> fetchMainCat() throws IOException {
		Out.print("---- start fetch all categores!");
		long time = System.currentTimeMillis();
		List<CategoryObj> mainCatList = new ArrayList<CategoryObj>();
		BrowseResponse browseResponse = service.browse();
		for (BrowseLink browseLink : browseResponse.getCategoryList()) {
			String[] splitedStrs = browseLink.getDataUrl().split("&cat=");
			CategoryObj obj = new CategoryObj(splitedStrs[splitedStrs.length - 1],browseLink.getName());
			mainCatList.add(obj);
			Out.print(obj.toString());
		}	
		Out.print("---- get all categories success! use " + (System.currentTimeMillis() - time)/1000 + " sec.");
		return mainCatList;
	}
	
	/**
	 * 获取所有分类
	 * 
	 * @param mainCatList
	 * @return map.key : 主类
	 *         map.value：所有子类
	 * @throws IOException
	 */
	private Map<CategoryObj,List<CategoryObj>> fetchAllSubCat(List<CategoryObj> mainCatList) throws Exception{
		Out.print("---- start fetch all sub categories!");
		long time = System.currentTimeMillis();
		Map<CategoryObj,List<CategoryObj>> catMap = new HashMap<CategoryObj,List<CategoryObj>>();
		for(CategoryObj mainCat : mainCatList){
			List<CategoryObj> subCatList = fetchSubCat(mainCat);
			catMap.put(mainCat, subCatList);
		}
		Out.print("---- get all sub categories success! use " + (System.currentTimeMillis() - time)/1000 + " sec.");
		return catMap;
	}
	
	private List<CategoryObj> fetchSubCat(CategoryObj mainCat) throws Exception{
		List<CategoryObj> subCatList = new ArrayList<CategoryObj>();
		ListResponse listResponse = service.list(mainCat.getId(), null, null,
				null);
		Out.print(mainCat.getName() + " sub cat:");
		for (DocV2 child : listResponse.getDocList()) {
			CategoryObj obj = new CategoryObj(child.getDocid(),child.getTitle().toString());
			subCatList.add(obj);
			Out.print(obj.toString());
		}		
		return subCatList;
	}
}
