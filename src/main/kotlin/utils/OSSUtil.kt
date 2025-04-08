package com.miaoyongzheng.utils

import com.aliyun.oss.*
import com.aliyun.oss.common.auth.CredentialsProviderFactory
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider
import com.aliyun.oss.common.comm.SignVersion
import com.aliyun.oss.model.CannedAccessControlList
import com.aliyun.oss.model.OSSObject
import com.aliyun.oss.model.ObjectListing
import com.aliyun.oss.model.PutObjectRequest
import java.io.*
import java.util.*


class OSSUtil {
    companion object {
        // OSS Bucket 名称前缀
        private const val BUCKET_NAME = "medintel"
        private const val ENDPOINT = "https://oss-cn-shanghai.aliyuncs.com"
        private const val REGION = "cn-shanghai"
        private val credentialsProvider: EnvironmentVariableCredentialsProvider =
            CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider()

        // 上传文件到 OSS
        fun uploadFileToOSS(inputStream: InputStream): String? {
            var url: String? = null
            val filename = UUID.randomUUID().toString() + ".png"
            // 创建 OSSClient 实例
            val clientBuilderConfiguration: ClientBuilderConfiguration = ClientBuilderConfiguration()
            // 显式声明使用 V4 签名算法
            clientBuilderConfiguration.signatureVersion = SignVersion.V4
            val ossClient: OSS = OSSClientBuilder.create()
                .endpoint(ENDPOINT)
                .credentialsProvider(credentialsProvider)
                .region(REGION)
                .build()

            try {
                // 设置存储空间的读写权限。读写权限ACL设置为私有PublicReadWrite
                ossClient.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicReadWrite);
                // 创建PutObjectRequest对象。
                val putObjectRequest = PutObjectRequest(BUCKET_NAME, filename, inputStream)
                // 创建PutObject请求。
                val result = ossClient.putObject(putObjectRequest)
                // 上传没报错就更新url
                url = "https://$BUCKET_NAME.oss-cn-shanghai.aliyuncs.com/$filename"
            } catch (oe: OSSException) {
                println(
                    "Caught an OSSException, which means your request made it to OSS, "
                            + "but was rejected with an error response for some reason."
                );
                println("Error Message: " + oe.errorMessage);
                println("Error Code:       " + oe.errorCode);
                println("Request ID:      " + oe.requestId);
                println("Host ID:           " + oe.hostId);
            } catch (ce: ClientException) {
                println(
                    "Caught an ClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with OSS, "
                            + "such as not being able to access the network."
                );
                println("Error Message: " + ce.message);
            } finally {
                // 关闭OSSClient。
                ossClient.shutdown()
            }
            return url
        }

        fun uploadFileToOSSDemo() {
            // 设置 OSS Endpoint 和 Bucket 名称
            val endpoint = "https://oss-cn-shanghai.aliyuncs.com"
            val bucketName = "medintel"

            // 替换为您的 Bucket 区域
            val region = "cn-shanghai"


            // 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量
            val credentialsProvider: EnvironmentVariableCredentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider()


            // 创建 OSSClient 实例
            val clientBuilderConfiguration: ClientBuilderConfiguration = ClientBuilderConfiguration()

            // 显式声明使用 V4 签名算法
            clientBuilderConfiguration.signatureVersion = SignVersion.V4
            val ossClient: OSS = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build()
            try {
                // 1. 创建存储空间（Bucket）
                ossClient.createBucket(bucketName)
                println("1. Bucket $bucketName 创建成功。")
                // 2. 上传文件
                val objectName = "medintel/example_object.txt"
                val content = "Hello OSS"
                ossClient.putObject(bucketName, objectName, ByteArrayInputStream(content.toByteArray()))
                println("2. 文件 $objectName 上传成功。")
                // 3. 下载文件
                val ossObject: OSSObject = ossClient.getObject(bucketName, objectName)
                val contentStream: InputStream = ossObject.objectContent
                val reader = BufferedReader(InputStreamReader(contentStream))
                var line: String?
                println("3. 下载的文件内容：")
                while ((reader.readLine().also { line = it }) != null) {
                    println(line)
                }
                contentStream.close()
                // 4. 列出文件
                println("4. 列出 Bucket 中的文件：")
                val objectListing: ObjectListing = ossClient.listObjects(bucketName)
                for (objectSummary in objectListing.objectSummaries) {
                    println(" - " + objectSummary.key + " (大小 = " + objectSummary.size + ")")
                }
                // 5. 删除文件
                ossClient.deleteObject(bucketName, objectName)
                println("5. 文件 $objectName 删除成功。")
                // 6. 删除存储空间（Bucket）
                ossClient.deleteBucket(bucketName)
                println("6. Bucket $bucketName 删除成功。")
            } catch (oe: OSSException) {
                println(
                    "Caught an OSSException, which means your request made it to OSS, "
                            + "but was rejected with an error response for some reason."
                )
                println("Error Message:" + oe.errorMessage)
                println("Error Code:" + oe.errorCode)
                println("Request ID:" + oe.requestId)
                println("Host ID:" + oe.hostId)
            } catch (ce: ClientException) {
                println(
                    ("Caught an ClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with OSS, "
                            + "such as not being able to access the network.")
                )
                println("Error Message:" + ce.message)
            } catch (ce: IOException) {
                println(
                    ("Caught an ClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with OSS, "
                            + "such as not being able to access the network.")
                )
                println("Error Message:" + ce.message)
            } finally {
                ossClient.shutdown()
            }
        }


    }
}