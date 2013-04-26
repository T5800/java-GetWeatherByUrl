String city = wxMsg.getContent().substring(0, 2);
          URL url = null;
        	if(city.equals("杭州")) {
        		url = new URL("http://www.weather.com.cn/weather/101210101.shtml");
        	}else if(city.equals("仙居")) {
        		url = new URL("http://www.weather.com.cn/weather/101210606.shtml");
        	}
    		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
    		String str=""; //
    		String china = "[\u4e00-\u9fa5]+";
    		String temperature = "[0-9]+℃";
    		String fen = "[\u4e00-\u9fa5]+风";
    		Pattern p = null;
    		Matcher m = null;
    		int count_tianqi = 0;//用于记录白天0，夜间1。
    		int count_wendu = 0;
    		List<String> local_tianqi = new ArrayList<String>(5);
    		List<String> local_wendu = new ArrayList<String>(5);
    		int day = 0 ;
    		while( (str = br.readLine()) != null) {
    			str = str.trim();
    			int tianqi = str.indexOf("<a href =\"http://baike.weather.com.cn/index.php?doc-view-");
    			int wendu = str.indexOf("<strong>");
      			if(str.endsWith("</title>")){
      				p = Pattern.compile(china);
      				m = p.matcher(str);
      				while(m.find()) {
      					System.out.println(m.group());
      				}
      			}else{
    				if(	tianqi != -1 ){
    					/*下面匹配天气 */
    					str = str.substring(tianqi);
    					p = Pattern.compile(china);
    					m = p.matcher(str);
    					while(m.find()) {
    						local_tianqi.add(m.group());
    						count_tianqi++;
    					}
    				}else if( wendu != -1 ) {
    					/*下面去匹配气温*/
    					str = str.substring(wendu);
    					p = Pattern.compile(temperature);
    					m = p.matcher(str);
    					while(m.find()) {
    						local_wendu.add(m.group());
    						count_wendu++;
    					}
    					if(count_tianqi == 2) {
    						++day;
    						String s = day==1?"今天":day==2?"明天":"后天";
    						final_tianqi += s;
    						System.out.println(s);
    						if(local_tianqi.get(0).equals(local_tianqi.get(1))) {
    							s = local_tianqi.get(0);
    							final_tianqi += s;
    							System.out.println(s);
    						}else {
    							String total_tianqi = local_tianqi.get(0) + "转" + local_tianqi.get(1);
    							final_tianqi += total_tianqi;
    							System.out.println(total_tianqi);
    						}
    						String total_wendu = local_wendu.get(0) + "到" + local_wendu.get(1);
    						final_tianqi += total_wendu ;
    						if(day < 3){
    							final_tianqi += ",";
    						}
    						System.out.println(total_wendu);
    						System.out.println();
    						count_tianqi = 0 ;
    						count_wendu = 0 ; 
    						local_wendu.clear();
    						local_tianqi.clear();
    						
    					}
    				}
//    			}
    			if(day == 3){
    				System.out.println(final_tianqi);
    				System.out.println("sender:" + wxMsg.getSender());
    				System.out.println("receiver: " + wxMsg.getReceiver());
    				request.setAttribute("content",	final_tianqi);
    				break;
    				
    			}
    		}
