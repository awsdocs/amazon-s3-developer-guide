# Upload an object using the AWS SDK for C\+\+<a name="UploadObjSingleCpp"></a>

**Example**  
The following C\+\+ code example puts a string into an Amazon S3 object using `PutObjectRequest` request by taking a text string as sample object data on a specified S3 bucket, and AWS Region\(optional\)\.  

```
#include <aws/core/Aws.h>
#include <aws/s3/S3Client.h>
#include <aws/s3/model/PutObjectRequest.h>
#include <iostream>
#include <fstream>

bool put_string_into_s3_object(const Aws::String& s3_bucket_name,
    const Aws::String& s3_object_name,
    const std::string& object_contents,
    const Aws::String& region = "")
{
    // If an AWS Region is specified, use it.
    Aws::Client::ClientConfiguration clientConfig;
    if (!region.empty())
        clientConfig.region = region;

    Aws::S3::S3Client s3_client(clientConfig);
    Aws::S3::Model::PutObjectRequest object_request;

    object_request.SetBucket(s3_bucket_name);
    object_request.SetKey(s3_object_name);
    const std::shared_ptr<Aws::IOStream> input_data =
        Aws::MakeShared<Aws::StringStream>("");
    *input_data << object_contents.c_str();
    object_request.SetBody(input_data);

    // Put the string into the S3 object.
    auto put_object_outcome = s3_client.PutObject(object_request);
    if (!put_object_outcome.IsSuccess()) {
        auto error = put_object_outcome.GetError();
        std::cout << "ERROR: " << error.GetExceptionName() << ": "
            << error.GetMessage() << std::endl;
        return false;
    }
    return true;
}

int main(int argc, char** argv)
{

    Aws::SDKOptions options;
    Aws::InitAPI(options);
    {
        // Assign these values before running the program.
        const Aws::String bucket_name = "BUCKET_NAME";
        const Aws::String object_name = "OBJECT_NAME";
        const std::string object_contents = "Put this text into the object.";
        const Aws::String region = ""; // Optional.

        // Put the file into the S3 bucket.
        if (put_string_into_s3_object(bucket_name, object_name, object_contents, region)) {
            std::cout << "The string was put into the object " << object_name
                << " in S3 bucket " << bucket_name << std::endl;
        }
    }
    Aws::ShutdownAPI(options);
}
```