<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试页</title>
    <style>
        div > button{
            width: 400px;
            height: 150px;
            margin: 100px auto;
            display: block;
            font-size: 40px
        }
    </style>
</head>
<body>
<div style="width: 100%">
    <button onclick="getOauth()" style="width: 200px;height: 150px;margin: 100px auto;display: block;font-size: 40px">
        信息授权
    </button>
    <button onclick="alipayUserAgreementPageSign()"
            >支付授权
    </button>
    <button onclick="alipayUserAgreementQuery()"
            >授权查询
    </button>
    <button onclick="alipayUserAgreementUnsign()"
            >解除授权
    </button>
    <button onclick="aliAgreementTradePay()"
            >免密付款（0.01元）
    </button>
    <button onclick="aliTradeRePay()"
            >欠款还款（暂未开通）
    </button>
    <button onclick="aliAgreementRepayment()"
            >免费还款
    </button>
</div>
<script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
<script>
    function getOauth() {
        var formData = {redirectUrl: "http://24z00278u5.qicp.vip/api/aliLogin"}
        $.post("/api/getOauth", formData, function (res) {
            console.log(res);
            if (res.code == '20000') {
                window.location.href = res.data;
            }
        }, "json");
    }

    function alipayUserAgreementUnsign() {
        //授权后才能使用。否则默认空值，使用默认userId测试
        var userId =  ${Session["userId"]!'0'};
        var formData = {userId: userId};
        $.post("/api/alipayUserAgreementUnsign", formData, function (res) {
            console.log(res);
            const div = document.createElement('divform');
            div.innerHTML = res.value.body;
            document.body.appendChild(div);
            document.forms[0].acceptCharset = 'UTF-8';//保持与支付宝默认编码格式一致，如果不一致将会出现：调试错误，请回到请求来源地，重新发起请求，错误代码 invalid-signature 错误原因: 验签出错，建议检查签名字符串或签名私钥与应用公钥是否匹配
            document.forms[0].submit();
        }, "json");
    }

    function alipayUserAgreementPageSign() {
        //授权后才能使用。否则默认空值，使用默认userId测试
        var userId =  ${Session["userId"]!'0'};
        var formData = {userId: userId};
        $.post("/api/alipayUserAgreementPageSign", formData, function (res) {
            console.log(res);
            // const div = document.createElement('divform');
            // div.innerHTML = res.value.body;
            // document.body.appendChild(div);
            // document.forms[0].acceptCharset = 'UTF-8';//保持与支付宝默认编码格式一致，如果不一致将会出现：调试错误，请回到请求来源地，重新发起请求，错误代码 invalid-signature 错误原因: 验签出错，建议检查签名字符串或签名私钥与应用公钥是否匹配
            // document.forms[0].submit();

            window.location.href = res.value;
        }, "json");
    }


    function alipayUserAgreementQuery() {
        //授权后才能使用。否则默认空值，使用默认userId测试
        var userId =  ${Session["userId"]!'0'};
        var formData = {userId: userId};
        $.post("/api/alipayUserAgreementQuery", formData, function (res) {
            alert(res.value.body.alipay_user_agreement_query_response.sub_msg)
        }, "json");
    }

    function aliAgreementTradePay() {
        //测试写死自己的userid
        var userId =  ${Session["userId"]!'0'};
        var formData = {userId: userId};
        $.post("/api/aliAgreementTradePay", formData, function (res) {
            if(res.value.msg == 'success'){
                alert("支付成功！")
            }
        }, "json");
    }

    function aliTradeRePay() {
        //授权后才能使用。否则默认空值，使用默认userId测试
        var userId =  ${Session["userId"]!'0'};
        var formData = {userId: userId};
        $.post("/api/aliTradeRePay", formData, function (res) {
            console.log(res);
            const div = document.createElement('divform');
            div.innerHTML = res.value.body;
            document.body.appendChild(div);
            document.forms[0].acceptCharset = 'UTF-8';//保持与支付宝默认编码格式一致，如果不一致将会出现：调试错误，请回到请求来源地，重新发起请求，错误代码 invalid-signature 错误原因: 验签出错，建议检查签名字符串或签名私钥与应用公钥是否匹配
            document.forms[0].submit();
        }, "json");
    }

    function aliAgreementRepayment() {
        //测试写死自己的userid
        var userId =  ${Session["userId"]!'0'};
        var formData = {userId: userId};
        $.post("/api/aliAgreementRepaymentAsnc", formData, function (res) {
            if(res.value.msg == 'success'){
                alert("支付成功！")
            }
        }, "json");
    }
</script>


</body>
</html>