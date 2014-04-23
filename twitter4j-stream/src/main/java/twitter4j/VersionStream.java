/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class VersionStream {
    private static final String VERSION = "4.0.1";
    private static final String TITLE = "Twitter4J Streaming API support";
    private static ConfigurationBuilder config;

    private VersionStream() {
        throw new AssertionError();
    }

    public static String getVersion() {
        return VERSION;
    }

    /**
     * prints the version string
     *
     * @param args will be just ignored.
     */
    public static void main(String[] args) {
        //Setting OAuth Configurations
        config = new ConfigurationBuilder();
        config.setDebugEnabled(true)
                .setOAuthConsumerKey("sfiyyXHUfBqhqYJicvA")
                .setOAuthConsumerSecret("qbYHDtWltMZ2pvYxCFlbOyvSDguO7mcFPEvrabaC5w")
                .setOAuthAccessToken("16996040-Nv4YBi8m7HvR2Zfm0kUCuqCtceBHhVMuy8Eap94lJ")
                .setOAuthAccessTokenSecret("XN2KBxAB74NL94jicyW2GYy0hIQzozCP5BqkxqtAqM");
        
        /* STREAM TWEETS */
        TwitterStream twitterStream = new TwitterStreamFactory(config.build()).getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                
                //Store Tweet in Database
                /*
                tweetModel tw = new tweetModel();
                tw.setStatusId(String.valueOf(status.getId()));
                tw.setUsername(status.getUser().getScreenName());
                tw.setMessage(status.getText().toString());
                
                if(status.isRetweet()){
                    tw.setRetweetCount(status.getRetweetedStatus().getRetweetCount());
                    System.out.println("RT = " + status.getRetweetedStatus().getRetweetCount());
                } else{
                    tw.setRetweetCount(status.getRetweetCount());
                }
                
                tw.setLatitude(status.getGeoLocation().latitude);
                tw.setLonghitude(status.getGeoLocation().longitude);
                tw.setDate(status.getCreatedAt().toGMTString());
                
                tw.setMessage(tweetHandler.RewriteTweet(tw.getMessage()));   //Rewrites Tweet for Emoji Statuses
                tweetHandler.addTweet(tw);
                */
                
                //Print Tweet
                System.out.println("@" + status.getUser().getScreenName() + 
//                        "\n..ID| " + status.getId() +
//                        "\n.SRC| " + status.getSource() +
                        "\n..TW| " + status.getText() +
//                        "\n..LL| " + status.getGeoLocation().getLatitude() + " " + status.getGeoLocation().getLongitude() +
//                        "\n..PL| " + status.getPlace() +
                        "\n" );
                
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        ManilaStreamer(twitterStream);   //Start streaming w/ GeoLocation
        
        System.out.println(TITLE + " " + VERSION);
    }
    
    private static void ManilaStreamer(TwitterStream twitterStream){
        //Setting Location Coordinates and Retrieving Tweets
        double[][] manilaBox = new double[][]{
            {120.90, 14.370583},    //sw
            {121.81, 14.583333}     //ne
        };
        
        FilterQuery query = new FilterQuery().locations(manilaBox);
        twitterStream.filter(query);
        
        //Closing Stream
//        try {
//            Thread.sleep(3000000);    
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        
//        twitterStream.cleanUp();
//        twitterStream.shutdown();
        
    }
}
