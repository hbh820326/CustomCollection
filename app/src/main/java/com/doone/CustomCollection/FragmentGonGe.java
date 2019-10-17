package com.doone.CustomCollection;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doone.CustomCollection.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentGonGe extends Fragment {
    String json = "{" +
            "    'status': 1," +
            "    'msg': ''," +
            "    'data': [" +
            "        {" +
            "            'pk_psndoc': '841266F6EB574D7FAAEB5B3CE34E9BA3'," +
            "            'usercode': '841266F6EB574D7FAAEB5B3CE34E9BA3'," +
            "            'name': '系统管理员'," +
            "            'code': 'zh'," +
            "            'mobile': '15059152911'," +
            "            'sex': 'M'," +
            "            'pk_dept': '20CDB4E88D174B31AC9FAD5960A67DAD'," +
            "            'deptname': '财务中心'," +
            "            'pk_org': 'EF801A3C90094B11BF48F05A3D05CB7B'," +
            "            'orgname': '恒亿集团有限公司'," +
            "            'isFriend': 'N'," +
            "            'cardid': '15059152911'," +
            "            'imid': '841266F6EB574D7FAAEB5B3CE34E9BA3'," +
            "            'pk_company': '42A0D1E4957A449BBE78238AAC3DAF7C'," +
            "            'isUploadHeadImg': '1'," +
            "            'moduleList': [" +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/B292E44EC3B43968/fwsq.png'," +
            "                    'text': '付款审批'," +
            "                    'nativeName': 'oafksp'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '23B821D2832C4C0C80B2FA3A52B7CCD3'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=wVdPQ27gV7s6codNUfo6z2e7MnEy0nW%2FulLoc%2FwL2AMMv%2FcFNUGd5rBxo3FG7rveu6gLInANKAMunPqdL0k6iDulc1iWGPJxLY4CDwWBx7aN4r5h%2BbBEzrqi2DRSK0XMK2xvrvX5MH8cY981SsGYo%2FRiu34NV4lUoTYYIyn4G%2BA%3D'," +
            "                    'id': '23B821D2832C4C0C80B2FA3A52B7CCD3'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://120.34.95.130:8880/upload/module/20FEF5DB1AFE308A/fybxd.png'," +
            "                    'text': '费用报销'," +
            "                    'nativeName': 'fybxd'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'E280D3BFA0794750913B33D9ACF543BA'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=UNvpF%2B%2Bdw%2BmQWVL0DsmIWFo4tb8YzkqohcmErCRJgWsT2emggZHvj9KD0XxfwfVaC%2B54dLONrhwfVeP3C7HxvlE0Q97kcHQ7qXk54WGmSlh0lRv%2BXtE4fqjt1KnG1KDWRmyAhLX0R3iZh0Gpl7a9AX14AxkFVJE%2Fyu32OR97SC0%3D'," +
            "                    'id': 'E280D3BFA0794750913B33D9ACF543BA'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://120.34.95.130:8880/upload/module/79B17057F36200EB/wpgh.png'," +
            "                    'text': '物品归还'," +
            "                    'nativeName': 'wpgh'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '5101308D1C6F406E83281FE586968D40'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=WPlDq9usJHX9Pa5bsn%2BI%2FDQ3JyO%2FTI6DNHIa%2BghSYBwJEEJXs%2FD2NpbfiOJGBti%2BtXxYdsMhNMXvcbFzIpe8lWxfVbzeaJVDODUsgKqNhiUNrE00g0DTpjyMC1oy9LK1GzhKd6NNfL3hvMfCh%2BPNuCItVFAtr3o%2BUbowAZ9l9hI%3D'," +
            "                    'id': '5101308D1C6F406E83281FE586968D40'," +
            "                    'category': '办公物品'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': '1'," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': '常用'," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://120.34.95.130:8880/upload/module/6D1AA1821940A577/yyjc.png'," +
            "                    'text': '用印借出'," +
            "                    'nativeName': 'yyjc'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '596C2C3CE3FC46DC85792983E1805A7B'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=LeSeT%2FrETLrhiPbIYglfziPLW70tcNPY20QDr5a%2F1b6w%2BrOw6dTM1EaxWl4kEQuaqi6XWgSJRkbZUWzHkqMWvIG%2FyvyBWntC3Jfwxuh%2BmYPwDJ9o1e9VBfdtPl5PeUfbYCjSY8XPyvX1vw8kSxFXazg0STmi%2FoUbhqqZ9aSF%2FtM%3D'," +
            "                    'id': '596C2C3CE3FC46DC85792983E1805A7B'," +
            "                    'category': '企业印章'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/128F1715A004E956/gzrz.png'," +
            "                    'text': '工作日志'," +
            "                    'nativeName': 'GZRZ'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 10," +
            "                    'module_type': '1'," +
            "                    'relationcode': '5CB73F592EF346F098C181FD42A3D2AD'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/pages/oa/daily/mobile/daily.html'," +
            "                    'id': '5CB73F592EF346F098C181FD42A3D2AD'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/FCEB14B97CE3100E/hyssq.png'," +
            "                    'text': '会议室申请'," +
            "                    'nativeName': 'hyssq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'A938740E5EFB4415BB49419F85AF9502'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=iUDzZky7en4%2BLPioLn7E46yLZva36oNPSWJEBDwMNoStfM7170ve0PT%2BLIwteIqu%2BbIHhKhQjetnNsSaMUPj3Taeol4gnWaZuUQhIeG5c0pNABz9Q5wKw%2F91IYbYT%2BFtwZicgolQFdDyEnKhARxnPcRlpz%2FSeO%2BD0P1bIgW291g2KfZJWFWZag9Z4CGCyothObBShsmehnGkYS%2FMTe524EZI7fFktGJnca5eI7YMn2FS7xA0yHdtWQMUNP5JGLz0WEZm9QCXNhkbVhbdM5VsGyGKiBh4KAdYjGCxQMEUx6vTTH59qvBGkZcYEG2ycKeMFP6ZFd3Ik0p%2FOXp8OeUskg%3D%3D'," +
            "                    'id': 'A938740E5EFB4415BB49419F85AF9502'," +
            "                    'category': '会议管理'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://120.34.95.130:8880/upload/module/71F06C5863D87F98/jkd.png'," +
            "                    'text': '借款单'," +
            "                    'nativeName': 'jkd'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'AD72D16BFF9245E58395D42EF00D0EDD'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=C9UT85F7BIGzh0q0W2dgbR9t4HLa5S6vrZoFaQwuqFkbXX8RHVE7HzvnsEmKKJsoSlYcBjg5edGhpR9TSi2twc8dKABM4q3BJxgf%2FdoLpxYty3Fxo1C6G4pDohsVn2EZ4fzxhCubkuDMRwjtlNRuZRWKteD0bUv3MqyfRUm2pXs%3D'," +
            "                    'id': 'AD72D16BFF9245E58395D42EF00D0EDD'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/6A4D5C699A55D267/gzlxd.png'," +
            "                    'text': '工作联系单'," +
            "                    'nativeName': 'gzlxd'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'B1FE711388F94B41A0DCED39AD1468A0'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=JDX7NWOwDLDgmnybOF9p3k%2FA4rK%2B2qAEi2npPbtE072dwOH6KFqKf3X58XJI4wGAuUWUersfuFKjcw6xSm8iqrrCzG1UuE0uhsLLVXYRLj%2Fo%2BxSX%2FoZILnz8571rpEzGkOVjAm0gXB%2FjNOzF8o6dQa3bt5%2F9TbGqWQCy1OxWbuhfWpoLOgKM9X9pLtYq%2BzSWWAHak%2FApSaGa3josmHie1aklHSZFz%2Fasy%2BQ8CT4Nye2Rj3VCOKlPlKbNWGRUqQvurpkIvJOyO0up3dXQTK1yFfqaQ2Grbw4%2BMdsIf7RpxpBIiLjGOC0Z%2BPGCMaOogMAhaMy78x%2F5FEY1tOJB4mHofQ%3D%3D'," +
            "                    'id': 'B1FE711388F94B41A0DCED39AD1468A0'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/97763DF4943B249/zhoujihua.png'," +
            "                    'text': '周报'," +
            "                    'nativeName': 'GZZB'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': 'BF35E9C0D0C7486F82D3290B89264843'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/pages/oa/weekly/mobile/weekly.html'," +
            "                    'id': 'BF35E9C0D0C7486F82D3290B89264843'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': '5'," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': '常用'," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://120.34.95.130:8880/upload/module/652346EFA9F87D5B/wpjy.png'," +
            "                    'text': '物品借用'," +
            "                    'nativeName': 'wpjy'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'C7DDD778F1F54329B6FDBF107ECFDBAC'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=Z0mfa0mLWy2JYzxDO6gk6FnXo0DkruoZ9ingV5Kl2e4iF1A4ddtIUS6mrD02mxjVao0EGxBuBq9fCi7AMNX8o3taSCefhwZx%2BU6mO2LkPI3S1VyMDozhecLYNcSulHhB4WmN0tfPkYU%2BTOc6KoZYSn%2BDBsX9gJBgQXmC3uq5XD8%3D'," +
            "                    'id': 'C7DDD778F1F54329B6FDBF107ECFDBAC'," +
            "                    'category': '办公物品'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/C5B324CB814C81E9/fwsq.png'," +
            "                    'text': '收文审批'," +
            "                    'nativeName': 'fwsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 5," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'E24396C2F997459DADEFDF0635CFCE59'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=WfBYAxDLm4zeU7CaQhNpwQ3dk5sYFswUltaGWHKavGXkU3FgnSsYXq5l%2FzanLSc9LHgq%2FUJZ9KAzCrUObLfc1MszmKXe2t2WD%2Fp8B8mUDV2b4qkaXKTkJoeV8NqrMNMDx3cDTZBT%2F%2BjQVR54YYWa48XnvHf8L%2FofxLdEhoHc73U%3D'," +
            "                    'id': 'E24396C2F997459DADEFDF0635CFCE59'," +
            "                    'category': '公文管理'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/FF8129EB07798523/cgsq.png'," +
            "                    'text': '采购申请'," +
            "                    'nativeName': 'cgsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '0176533E2556489A82DD0066EC601567'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=0zFtb67g61scJtiqjLLjUYoZK7hMKD1euYaI6kQA%2FeeeDCcUjyGdMkn5N2sl5GY3JW7%2BGvq91664F4ahp5zrITqSfZ%2FLPG7Isj%2FeXT0jB4tNMDqnZ0BxdHm%2BLBYi%2BK%2FjgD9H8SAooIelky2XPCUnFoJxeyWhNWYlibgezLxmZBo%3D'," +
            "                    'id': '0176533E2556489A82DD0066EC601567'," +
            "                    'category': '办公物品'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': '2'," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': '常用'," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/FC2EE9761F14FD31/qjsq.png'," +
            "                    'text': '请假申请'," +
            "                    'nativeName': 'qjsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '158F5CDD665C494992C8FB26B3C005B3'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=BfgMxjDgOy1FfkXYy0EF5p47tHqv8mnRvMHwyQd51%2FNWLF5C0Tulwx62NV7gVS1ZFucbgygTqnkpfmqe7D%2Bzbt4%2F3fcyFsZNQbFBfR03fK%2B7MGc6Rk86DVAi35IvQ4GXxnzl2QHQlomLwMlT2RUTlRkPBr0i%2BxQEIM0fLguubj4%3D'," +
            "                    'id': '158F5CDD665C494992C8FB26B3C005B3'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': '3'," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': '常用'," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/21630E4A3C4FCCD4/wply.png'," +
            "                    'text': '物品领用'," +
            "                    'nativeName': 'wply'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '190A9ABE06534ADDB27EA82EB3129294'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=sAaLtYMGEizdah3bh%2BaE4TQ3xnQfGLY53BDsYx6Qf%2FUgz%2FQYJw87eNJPmbxli6t%2B777mFfFJhKzsgfxFBEfBS65L7A%2FlEpDZ30dE0T2aCfeHcSUXh3fN1zppBN%2Feb3Fs8icontGqNz3yFkJwjuG8IGVVw3MXGXB1njCWaO1rsqjs%3D'," +
            "                    'id': '190A9ABE06534ADDB27EA82EB3129294'," +
            "                    'category': '办公物品'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/8F399366321DE485/ppyz.png'," +
            "                    'text': '名片印制'," +
            "                    'nativeName': 'ppyz'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '20F831CC99AD4077AB7F479DAF1ACCA3'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=UrYl6RdhJXzsHDlrzHFi6edDVeNORG4%2FYiAdqrnkcNM4LzfAzNOWBQpULr99G2BGsYT%2F3O3cH8YxZ8xnn9FX%2F1KbHJWKuGvG8sViwsZaQFsvn8nn7a9l%2B%2BRhMC71YO6dziwoU2PyEsXTpg%2FgHhW%2F5hyBhfFC30TsFFN2TVgbRi0%3D'," +
            "                    'id': '20F831CC99AD4077AB7F479DAF1ACCA3'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/918CD32964954D25/fabp.png'," +
            "                    'text': '方案报批'," +
            "                    'nativeName': 'fabp'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '2405496F9F994DE2998C7F52B17B9E3A'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=vSdQGxU27oEPKN9SWmU7aH6pUecDmU8lavfxOnI1CZhsEH7mXIzO%2BQ5lzYgx43oFM0ikVwF6wJ8kcyGC7zX3PTzwlbXDTEj%2FlPQKWSAzjDeO6mo6SYFmSXxoUI%2BDGa2fVPWIwNoQtEtz4CaaFtjubSmRy5NIT8quFM7arXac9So%3D'," +
            "                    'id': '2405496F9F994DE2998C7F52B17B9E3A'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/D2B136EAFA61AB4C/ycsq.png'," +
            "                    'text': '用车申请'," +
            "                    'nativeName': 'ycsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '3521A4B6E3874E5DAD5EA6387E2C2812'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=dNNVHrr8AQhs39n0%2B%2BEqmCOkkuSm0YdEaTOIWN2Iuf43AE7nILM9mignLZjArX%2F5mD5%2FCVanCmJMfcXWjjw3409ml8Hsw8jI%2FZOPWQoX%2Bx5wFJ3JMqnVJZYg37YG2qjSIdfpSC%2FQY%2FjO7bHmZRoorQpZVvd03xHYmPBujLUPAmE%3D'," +
            "                    'id': '3521A4B6E3874E5DAD5EA6387E2C2812'," +
            "                    'category': '车辆管理'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/7DDD96CF11881C65/fwsp.png'," +
            "                    'text': '发文审批'," +
            "                    'nativeName': 'fwsp'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 56," +
            "                    'module_type': '6'," +
            "                    'relationcode': '38225781A1A645AA823DC5F46736C730'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=XTbHTDbBfGg7ZwaX%2Fde6emcbnGD7kAf15jewFZJfCzgfs%2F%2FQujkuE8bSiRq2Pm%2FgpjqG8XgR55QzO0X1Wm%2BySQcLCWlCzy6kwJJJ6G4GPBckakmMsmO%2B5wEZSjPXwzkGTTbPRz9Xn3Jr3i7o2VYVMzeqvUroT1zMePL9LGcQogA%3D'," +
            "                    'id': '38225781A1A645AA823DC5F46736C730'," +
            "                    'category': '公文管理'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/86C72AE02525AD79/wcsq.png'," +
            "                    'text': '外出申请'," +
            "                    'nativeName': 'wcsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '5B9818AD7C5742A6B74ED607D54C880A'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=LrfFy1sWT833D6%2FYO9GjEopX82lBiz6RYMYRXkX4Ngjh5MOl%2BpxWyZE%2FddWG7oz3pGLE5ZGgPQJ8OsMDtdKuJ99dekldjdL5WyX6XjWNzfzvF2Ofd2VlgD3wXbdKBJMBFnxyciWI3IQIX%2FXkL1imsDcBbbWywVFHIM93Y5nobvw%3D'," +
            "                    'id': '5B9818AD7C5742A6B74ED607D54C880A'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/72CFFF6F7ABA8670/htsp.png'," +
            "                    'text': '合同审批'," +
            "                    'nativeName': 'htsp'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '5EA21997801B450DBB64C7A14419A783'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=ODRcaphxW2llAE%2BcILjnEMCCljuQVepTpHXKKkpFlG3B3WIbjUyvg39%2Bt6eK5XC7bJ2pebsyhM6qXFLL1lm4ktoQWNxzhVbF0vnbinTBUk8phdnzJr5h%2FKxqUlqfX3HuGUNJsQo2K49EhmeA4BTwajVTDEGfvLfKxwLrwZuDsD8%3D'," +
            "                    'id': '5EA21997801B450DBB64C7A14419A783'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/45CE07475B79B05B/lply.png'," +
            "                    'text': '礼品领用'," +
            "                    'nativeName': 'lply'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': '6EA7776659E044E9A057B982194A1D70'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=YjKvB4zNF6i9vv%2BN94Gk14SFaqj096W0ExglAjzXv3fsfPJQsfH%2Fzo8WXrKp%2FgooKkd81P4s0%2BcHLbYtG%2B4PbZ9hWTF4IgFKhHv%2ByBB6DJf6%2BDJc9gWfFc%2FlVC%2BQxmSnOCWarE5oZTjoDloZK0BP4OeuBJLKOvR4TitWknYCHXw%3D'," +
            "                    'id': '6EA7776659E044E9A057B982194A1D70'," +
            "                    'category': '办公物品'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': '4'," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': '常用'," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/ABCDA980DA80EBF3/hyjy.png'," +
            "                    'text': '会议纪要'," +
            "                    'nativeName': 'hyjy'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 33," +
            "                    'module_type': '6'," +
            "                    'relationcode': '981C352C34534ADD8F1FC754A3D90F89'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=SZRoHCLG2Ea5iuVQ9DpPMvmmZJxz3ZM8ARgWjpgA2UWeGfk2%2BYG%2BG2%2BgbJ9b%2Bv%2FqonG6NXLkNv3wFnzux6YdVZmI%2BQSKG8YNdU3SKjlLXnN03EBo%2FVz%2B6RoxA8%2B1uQS8vrGDkl8fg8UhiWXLqXuULwTXxIZTrPBF9cF24aP9L34%3D'," +
            "                    'id': '981C352C34534ADD8F1FC754A3D90F89'," +
            "                    'category': '会议管理'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/7E16B2828400BDFA/yysq.png'," +
            "                    'text': '用印申请'," +
            "                    'nativeName': 'yysq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'B3974C2CBA03410B9CD0407348BE5428'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=ElaOw54SnPI72FO5XLr%2BWijzYRotMdCeA98nok%2F5jjYp7Y1DGOR4GSNqhvsCHQhmBTNtWFcxkVfzvS0srTjAGhcWIZKddv5G4WSCCpmsDAk5AbU8X90qTlehqrEvi%2F5OWclS38LTNHk%2FzdPSSt%2FRWs0Pud%2Fss%2Fvv5RLaB74CQTg%3D'," +
            "                    'id': 'B3974C2CBA03410B9CD0407348BE5428'," +
            "                    'category': '企业印章'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/F790F2F3F8F1EBFB/kzsq.png'," +
            "                    'text': '刻章申请'," +
            "                    'nativeName': 'kzsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'DA7E6AB25FF94A2EA931F965D7A9363D'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=Y5fu1phbmX2dnmqhmfMU4PWiDaf3pMFtTJp%2Fl%2Fll2BnuX9Q1mH2YuiNd8gSgkLCaVIeQ4FV2MlacL%2FG8KHGPGFHwmAaTLo46HNKnFWFKGyqtRtmT%2BU9EAi0XPpd6A9kdYCXZ%2BLxovODYRTR%2BAefUawX8txmEyIqPX9l4Vbz%2Bn%2Bg%3D'," +
            "                    'id': 'DA7E6AB25FF94A2EA931F965D7A9363D'," +
            "                    'category': '企业印章'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/D5DACF671BD6A255/ccsq.png'," +
            "                    'text': '出差申请'," +
            "                    'nativeName': 'ccsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 100," +
            "                    'module_type': '6'," +
            "                    'relationcode': 'D3852C0C85334B50B95C99282528C2C2'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/jflow-web/zh/login/ssoLogin.do?token=hwwZbalyuoglm9uT7%2FBtfQ5kI9qvNuZ7eO1%2BxAWgcpEUX7w%2FQasBbcYB8yYn9yAQxAa2k4bzjCbJSZNkdqPV%2B7tJAFRpskubAZbthx4Hzly5dxAFtgA7MewSBMvV%2B8xWwnyT31DTtJIV233c2Uf7aar9a%2FCFmliNdTfPHeuQLzc%3D'," +
            "                    'id': 'D3852C0C85334B50B95C99282528C2C2'," +
            "                    'category': '日常行政'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': '2E23E19368234C5B911BCCBBFC1B8773'," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/C99D952CE18AF4F1/nc_fksq.png'," +
            "                    'text': '付款申请 '," +
            "                    'nativeName': 'nc_fksq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': '6A88472CEBE34FE48A8A6BD75C6CF907'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=36D1'," +
            "                    'id': '6A88472CEBE34FE48A8A6BD75C6CF907'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/A8345434227274B6/nc_fkjs.png'," +
            "                    'text': '付款结算'," +
            "                    'nativeName': 'nc_fkjs'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': 'F267742E205340FAB4A7DFB2A165E6E4'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=D5'," +
            "                    'id': 'F267742E205340FAB4A7DFB2A165E6E4'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/653859CDE1BE6FCD/nc_rzsq.png'," +
            "                    'text': '入职申请'," +
            "                    'nativeName': 'nc_rzsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': 'CF7CD031E8C24097BE991A2661AC8625'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6101'," +
            "                    'id': 'CF7CD031E8C24097BE991A2661AC8625'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/C0A03ED4157F9941/nc_lzsq.png'," +
            "                    'text': '离职申请'," +
            "                    'nativeName': 'nc_lzsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': '38500DAC0E1A4428B30B75F4630DA8E6'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6115'," +
            "                    'id': '38500DAC0E1A4428B30B75F4630DA8E6'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/DFDE70196F14FF5C/nc_tpsq.png'," +
            "                    'text': '调配申请'," +
            "                    'nativeName': 'nc_tpsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': 'D09CF4FAF8A3402CBFC8812FFCEC6E83'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6113'," +
            "                    'id': 'D09CF4FAF8A3402CBFC8812FFCEC6E83'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/981BB7FF10626E37/nc_zzsq.png'," +
            "                    'text': '转正申请'," +
            "                    'nativeName': 'nc_zzsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': '93FD8A61867444C7A0B7C524732A41FB'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6111'," +
            "                    'id': '93FD8A61867444C7A0B7C524732A41FB'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/15B73186DF5350DF/nc_jzsq.png'," +
            "                    'text': '兼职申请'," +
            "                    'nativeName': 'nc_jzsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': 'BF9985DF3D934DDE9A21A98690861C91'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6117'," +
            "                    'id': 'BF9985DF3D934DDE9A21A98690861C91'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/D1FC30F94218BA11/nc_txsq.png'," +
            "                    'text': '调薪申请'," +
            "                    'nativeName': 'nc_txsq'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': '4F78E5CACBD540289E2B39650767992D'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6301'," +
            "                    'id': '4F78E5CACBD540289E2B39650767992D'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }," +
            "                {" +
            "                    'icon': 'http://oa.fjhengyi.com:8880/upload/module/126FFAF252D72530/nc_xzff.png'," +
            "                    'text': '薪资发放'," +
            "                    'nativeName': 'nc_xzff'," +
            "                    'screenStatus': '0'," +
            "                    'cont': 0," +
            "                    'module_type': '1'," +
            "                    'relationcode': 'F3135F1EA3434ECB964237A5BAC9D4C5'," +
            "                    'url': 'http://oa.fjhengyi.com:8880/mapPage.do?path=demo/index&billtype=6302'," +
            "                    'id': 'F3135F1EA3434ECB964237A5BAC9D4C5'," +
            "                    'category': 'NC办公'," +
            "                    'isFolder': false," +
            "                    'displayType': 0," +
            "                    'nativeHead': '0'," +
            "                    'categorySort': null," +
            "                    'thirdPartyApp': ''," +
            "                    'thirdPartyId': ''," +
            "                    'commonly': null," +
            "                    'homePage': '0'" +
            "                }" +
            "            ]" +
            "        }" +
            "    ]," +
            "    'code': 200" +
            "}";
List <List<Map<String, Object>>>reList=new ArrayList<>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Map<String, Object> data = new Gson().fromJson(json, Map.class);
        List<Map<String, Object>> list= (List<Map<String, Object>>) data.get("data");
        Map<String, Object> data0 =list.get(0);
        List<Map<String, Object>> moduleList= (List<Map<String, Object>>) data0.get("moduleList");
        reList.add(moduleList.subList(0,10));
        reList.add(moduleList.subList(10,15));
        reList.add(moduleList.subList(15,moduleList.size()));

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ggv_view, container, false);
        RecyclerView listView=view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        listView.setAdapter(new RecyclerView.Adapter() {
            GonGeView.SelectListener select=    new GonGeView.SelectListener() {
                @Override
                public void selectOn(Map map) {
                    Toast.makeText(getActivity(),map.toString(),Toast.LENGTH_SHORT).show();
                }
            };
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.ggv_child,viewGroup,false)){};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                GonGeView ggv= (GonGeView) viewHolder.itemView;
                ggv.setData(reList.get(i));
                ggv.setOnSelectListener(select);
            }

            @Override
            public int getItemCount() {
                return reList.size();
            }
        });
        return view;
    }
}
