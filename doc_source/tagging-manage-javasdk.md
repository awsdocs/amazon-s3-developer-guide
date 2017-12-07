# Managing Tags Using SDK \(AWS SDK for Java\)<a name="tagging-manage-javasdk"></a>

The following Java code example does the following:

+ Create an object with tags\.

+ Retrieve tag set\.

+ Update the tag set \(replace the existing tag set\)\.

For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.

```
package s3.amazon.com.docsamples;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

public class ObjectTaggingTest {
    
    static String bucketName = "***bucket***";
    static String keyName = "***object key name***";
    static String filePath = "***filepath***";

    public static void main(String[] args) {

        AmazonS3Client s3client = new AmazonS3Client(new BasicAWSCredentials("<AccessKey>", "<SecretKey>"));

        // 1. Put object with tags.
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, keyName, new File(filePath)); 
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("Key1", "Value1"));
        tags.add(new Tag("Key2", "Value2"));
        putRequest.setTagging(new ObjectTagging(tags));
        PutObjectResult putResult = s3client.putObject(putRequest);
        
        // 2. Retrieve object tags.
        GetObjectTaggingRequest getTaggingRequest = new GetObjectTaggingRequest(bucketName, keyName);
        GetObjectTaggingResult  getTagsResult = s3client.getObjectTagging(getTaggingRequest);
        
        // 3. Replace the tagset.
        List<Tag> newTags = new ArrayList<Tag>();
        newTags.add(new Tag("Key3", "Value3"));
        newTags.add(new Tag("Key4", "Value4"));
        s3client.setObjectTagging(new SetObjectTaggingRequest(bucketName, keyName, new ObjectTagging(newTags)));

        // 4. Retrieve object tags.
        GetObjectTaggingRequest getTaggingRequest2 = new GetObjectTaggingRequest(bucketName, keyName);
        GetObjectTaggingResult  getTagsResult2 = s3client.getObjectTagging(getTaggingRequest);
    }
}
```