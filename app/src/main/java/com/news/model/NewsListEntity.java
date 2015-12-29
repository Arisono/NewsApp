package com.news.model;

import java.util.List;

/**
 * @author Administrator
 * @功能:新闻列表
 */
public class NewsListEntity {

	private int showapi_res_code;
	private String showapi_res_error;
	private NewsListBody showapi_res_body;

	public class NewsListBody {
		private Pagebean pagebean;
		private int ret_code;

		public class Pagebean {
			private int allNum;

			private int allPages;

			private List<Contentlist> contentlist;

			private int currentPage;

			private int maxResult;

			public void setAllNum(int allNum) {
				this.allNum = allNum;
			}

			public int getAllNum() {
				return this.allNum;
			}

			public void setAllPages(int allPages) {
				this.allPages = allPages;
			}

			public int getAllPages() {
				return this.allPages;
			}

			public void setCurrentPage(int currentPage) {
				this.currentPage = currentPage;
			}

			public int getCurrentPage() {
				return this.currentPage;
			}

			public void setMaxResult(int maxResult) {
				this.maxResult = maxResult;
			}

			public int getMaxResult() {
				return this.maxResult;
			}

			public class Contentlist {

				private String channelId;

				private String channelName;

				private int chinajoy;

				private String desc;

				private List<ImageUrls> imageurls;

				private String link;

				private String long_desc;

				private String nid;

				private String pubDate;

				private String source;

				private String title;

				public class ImageUrls {
					private String url;
					private int height;
					private int width;

					public String getUrl() {
						return url;
					}

					public void setUrl(String url) {
						this.url = url;
					}

					public int getHeight() {
						return height;
					}

					public void setHeight(int height) {
						this.height = height;
					}

					public int getWidth() {
						return width;
					}

					public void setWidth(int width) {
						this.width = width;
					}

				}

				public String getChannelId() {
					return channelId;
				}

				public void setChannelId(String channelId) {
					this.channelId = channelId;
				}

				public String getChannelName() {
					return channelName;
				}

				public void setChannelName(String channelName) {
					this.channelName = channelName;
				}

				public int getChinajoy() {
					return chinajoy;
				}

				public void setChinajoy(int chinajoy) {
					this.chinajoy = chinajoy;
				}

				public String getDesc() {
					return desc;
				}

				public void setDesc(String desc) {
					this.desc = desc;
				}

				public List<ImageUrls> getImageurls() {
					return imageurls;
				}

				public void setImageurls(List<ImageUrls> imageurls) {
					this.imageurls = imageurls;
				}

				public String getLink() {
					return link;
				}

				public void setLink(String link) {
					this.link = link;
				}

				public String getLong_desc() {
					return long_desc;
				}

				public void setLong_desc(String long_desc) {
					this.long_desc = long_desc;
				}

				public String getNid() {
					return nid;
				}

				public void setNid(String nid) {
					this.nid = nid;
				}

				public String getPubDate() {
					return pubDate;
				}

				public void setPubDate(String pubDate) {
					this.pubDate = pubDate;
				}

				public String getSource() {
					return source;
				}

				public void setSource(String source) {
					this.source = source;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

			}

			public List<Contentlist> getContentlist() {
				return contentlist;
			}

			public void setContentlist(List<Contentlist> contentlist) {
				this.contentlist = contentlist;
			}

		}

		public Pagebean getPagebean() {
			return pagebean;
		}

		public void setPagebean(Pagebean pagebean) {
			this.pagebean = pagebean;
		}

		public int getRet_code() {
			return ret_code;
		}

		public void setRet_code(int ret_code) {
			this.ret_code = ret_code;
		}
		
		
	}

	public int getShowapi_res_code() {
		return showapi_res_code;
	}

	public void setShowapi_res_code(int showapi_res_code) {
		this.showapi_res_code = showapi_res_code;
	}

	public String getShowapi_res_error() {
		return showapi_res_error;
	}

	public void setShowapi_res_error(String showapi_res_error) {
		this.showapi_res_error = showapi_res_error;
	}

	public NewsListBody getShowapi_res_body() {
		return showapi_res_body;
	}

	public void setShowapi_res_body(NewsListBody showapi_res_body) {
		this.showapi_res_body = showapi_res_body;
	}

}
