import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

public class TestDemo01 {
    public static void main(String[] args) {
        String imgName = "http://prld54q8x.bkt.clouddn.com/0F995E58B45B44CA894539DE1F20F395_1.jpg";
        TestDemo01.delete(imgName);

    }

    public static void delete(String url){
        Auth auth = Auth.create("Nn0lfujcqbAA8-hpcv7juweVvpe_vPuJY0p9cTBN", "3Xd8cUp6q8QkqU8dniiRbCiUqsyeWIAs5nEArWDg");
        Configuration config = new Configuration(Zone.autoZone());
        BucketManager bucketMgr = new BucketManager(auth, config);
        int lastIndex = url.lastIndexOf("/");
        String imgName = url.substring(lastIndex+1);
        System.out.println(imgName);
        try {
            bucketMgr.delete("saas-ee88", imgName);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
