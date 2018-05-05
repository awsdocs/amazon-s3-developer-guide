# Making Requests Using Federated User Temporary Credentials \- AWS SDK for Ruby<a name="AuthUsingTempFederationTokenRuby"></a>

You can provide temporary security credentials for your federated users and applications so that they can send authenticated requests to access your AWS resources\. When requesting temporary credentials from the IAM service, you must provide a user name and an IAM policy that describes the resource permissions that you want to grant\. By default, the session duration is one hour\. However, if you are requesting temporary credentials using IAM user credentials, you can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\. For information about temporary security credentials for your federated users and applications, see [Making Requests](MakingRequests.md)\.

**Note**  
For added security when you request temporary security credentials for federated users and applications, you might want to use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, see [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.

**Example**  
The following Ruby code example allows a federated user with a limited set of permissions to lists keys in the specified bucket\.   

```
  1. require 'aws-sdk-s3'
  2. require 'aws-sdk-iam'
  3. 
  4. USAGE = <<DOC
  5. 
  6. Usage: federated_create_bucket_policy.rb -b BUCKET -u USER [-r REGION] [-d] [-h]
  7. 
  8.   Creates a federated policy for USER to list items in BUCKET for one hour.
  9. 
 10.   BUCKET is required and must already exist.
 11. 
 12.   USER is required and if not found, is created.
 13. 
 14.   If REGION is not supplied, defaults to us-west-2.
 15. 
 16.   -d gives you extra (debugging) information.
 17. 
 18.   -h displays this message and quits.
 19. 
 20. DOC
 21. 
 22. $debug = false
 23. 
 24. def print_debug(s)
 25.   if $debug
 26.     puts s
 27.   end
 28. end
 29. 
 30. def get_user(region, user_name, create)
 31.   user = nil
 32.   iam = Aws::IAM::Client.new(region: 'us-west-2')
 33.   
 34. begin
 35.   user = iam.create_user(user_name: user_name)
 36.   iam.wait_until(:user_exists, user_name: user_name)
 37.   print_debug("Created new user #{user_name}")
 38. rescue Aws::IAM::Errors::EntityAlreadyExists
 39.   print_debug("Found user #{user_name} in region #{region}")
 40. end
 41. end
 42. 
 43. # main
 44. region = 'us-west-2'
 45. user_name = ''
 46. bucket_name = ''
 47. 
 48. i = 0
 49. 
 50. while i < ARGV.length
 51.   case ARGV[i]
 52. 
 53.     when '-b'
 54.       i += 1
 55.       bucket_name = ARGV[i]
 56. 
 57.     when '-u'
 58.       i += 1
 59.       user_name = ARGV[i]
 60. 
 61.     when '-r'
 62.       i += 1
 63. 
 64.       region = ARGV[i]
 65. 
 66.     when '-d'
 67.       puts 'Debugging enabled'
 68.       $debug = true
 69. 
 70.     when '-h'
 71.       puts USAGE
 72.       exit 0
 73. 
 74.     else
 75.       puts 'Unrecognized option: ' + ARGV[i]
 76.       puts USAGE
 77.       exit 1
 78. 
 79.   end
 80. 
 81.   i += 1
 82. end
 83. 
 84. if bucket_name == ''
 85.   puts 'You must supply a bucket name'
 86.   puts USAGE
 87.   exit 1
 88. end
 89. 
 90. if user_name == ''
 91.   puts 'You must supply a user name'
 92.   puts USAGE
 93.   exit 1
 94. end
 95. 
 96. #Identify the IAM user we allow to list Amazon S3 bucket items for an hour.
 97. user = get_user(region, user_name, true)
 98. 
 99. # Create a new STS client and get temporary credentials.
100. sts = Aws::STS::Client.new(region: region)
101. 
102. creds = sts.get_federation_token({
103.   duration_seconds: 3600,
104.   name: user_name,
105.   policy: "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"Stmt1\",\"Effect\":\"Allow\",\"Action\":\"s3:ListBucket\",\"Resource\":\"arn:aws:s3:::#{bucket_name}\"}]}",
106. })
107. 
108. # Create an Amazon S3 resource with temporary credentials.
109. s3 = Aws::S3::Resource.new(region: region, credentials: creds)
110. 
111. puts "Contents of '%s':" % bucket_name
112. puts '  Name => GUID'
113. 
114.  s3.bucket(bucket_name).objects.limit(50).each do |obj|
115.       puts "  #{obj.key} => #{obj.etag}"
116. end
```