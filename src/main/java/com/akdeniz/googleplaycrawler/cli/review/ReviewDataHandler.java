package com.akdeniz.googleplaycrawler.cli.review;

import java.util.ArrayList;
import java.util.List;

import com.akdeniz.googleplaycrawler.GooglePlay.GetReviewsResponse;
import com.akdeniz.googleplaycrawler.GooglePlay.Review;
import com.akdeniz.googleplaycrawler.GooglePlay.ReviewResponse;
import com.akdeniz.googleplaycrawler.GooglePlayAPI;
import com.akdeniz.googleplaycrawler.GooglePlayAPI.REVIEW_SORT;
import com.akdeniz.googleplaycrawler.cli.model.ReviewModel;
import com.akdeniz.googleplaycrawler.cli.report.Out;

/**
 * 获取二级以下所有评论
 * @author sandro
 *
 */
public class ReviewDataHandler {

	private GooglePlayAPI service;
	
	private static final String[] PACKAGE_NAMES = new String[]{"com.tencent.mm"}; 
	
	public ReviewDataHandler(GooglePlayAPI service){
		this.service = service;
	}
	
	public void getReviews() throws Exception{
		for(String packageName : PACKAGE_NAMES){
			Out.print("---"+packageName +  " : ");
			List<ReviewModel> list = fetchReviews(packageName);
			saveFile(packageName,list);
		}
	}
	
	private void saveFile(String packageName,List<ReviewModel> list){
		if(packageName == null || packageName.length()==0){
			System.out.println("packageName is null");
			return;
		}
		
		if(list == null || list.size() == 0){
			Out.print("review Modle list is empty");
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		for(ReviewModel model : list){
			if(model.getStarRating()<=3){
				sb.append(model.toString());
			}
		}
		Out.saveFile(packageName + ".text", sb.toString());
	}
	
	private List<ReviewModel> fetchReviews(String packageName) throws Exception {
		List<ReviewModel> list = new ArrayList<ReviewModel>();
		int start = 0;
		int num = 20;
		int errortime = 0;
		int count = 1;
		while (true) {
			ReviewResponse reviews = null;
			try {
				reviews = service.reviews(packageName, REVIEW_SORT.HELPFUL,
						start, num);
			} catch (Exception e) {
				errortime++;
				if (errortime > 2) {
					System.err
							.println("*** get review response error 2 times,has to return ");
					return list;
				}
				System.err.println("*** get review response error : "
						+ e.toString() + " ,sleep 10 sec do it again!");

				Thread.sleep(10000);
				continue;
			}
			List<Review> reviewList = getReviewList(reviews);
			if(reviewList == null){
				return list;
			}else{
				for (Review r : reviewList) {
					ReviewModel model = new ReviewModel(r.getAuthorName(),r.getTitle(),r.getComment(),r.getStarRating(),r.getTimestampMsec());
					list.add(model);
					System.out.println(count + " >>>>>>>>>>>>>>>>>>");
					System.out.println(model.toString());
					count ++;
				}	
				Thread.sleep(3000);
				start +=num;
			}
		}
	}
	
	private List<Review>  getReviewList(ReviewResponse reviews) {
		try {
			GetReviewsResponse response = reviews.getGetResponse();
			if (response.getReviewCount() == 0) {
				System.out.println(" get review count is 0");
				return null;
			}
			List<Review> list = response.getReviewList();
			if (list == null
					|| list.size() == 0) {
				System.out.println(" get review list is null");
				return null;
			}else{
				return list;
			}
		} catch (Exception e) {
			System.out.println(" get review list error : " + e.toString());
			return null;
		}
	}
}
