# Upload an object using the AWS SDK for C\+\+<a name="UploadObjSingleCpp"></a>

**Example**  
The following C\+\+ code example puts a string into an Amazon S3 object using `PutObjectRequest` request by taking a text string as sample object data on a specified S3 bucket, and AWS Region\(optional\)\.  

```
#include <aws/core/Aws.h>
#include <aws/s3/S3Client.h>
#include <aws/s3/model/PutObjectRequest.h>
#include <iostream>
#include <fstream>
#include <awsdoc/s3/s3_examples.h>

bool AwsDoc::S3::PutObjectBuffer(const Aws::String& bucketName,
    const Aws::String& objectName,
    const std::string& objectContent,
    const Aws::String& region)
{
    Aws::Client::ClientConfiguration config;
    
    if (!region.empty())
    {
        config.region = region;
    }

    Aws::S3::S3Client s3_client(config);

    Aws::S3::Model::PutObjectRequest request;
    request.SetBucket(bucketName);
    request.SetKey(objectName);

    const std::shared_ptr<Aws::IOStream> input_data =
        Aws::MakeShared<Aws::StringStream>("");
    *input_data << objectContent.c_str();

    request.SetBody(input_data);

    Aws::S3::Model::PutObjectOutcome outcome = s3_client.PutObject(request);

    if (!outcome.IsSuccess()) {
        std::cout << "Error: PutObjectBuffer: " << 
            outcome.GetError().GetMessage() << std::endl;

        return false;
    }
    else
    {
        std::cout << "Success: Object '" << objectName << "' with content '"
            << objectContent << "' uploaded to bucket '" << bucketName << "'.";

        return true;
    }
}

int main()
{
    Aws::SDKOptions options;
    Aws::InitAPI(options);
    {
        const Aws::String bucket_name = "my-bucket";
        const Aws::String object_name = "my-file.txt";
        const std::string object_content = "This is my sample text content.";
        const Aws::String region = "us-east-1";

        if (!AwsDoc::S3::PutObjectBuffer(bucket_name, object_name, object_content, region)) 
        {
            return 1;
        }
    }
    Aws::ShutdownAPI(options);

    return 0;
}
```