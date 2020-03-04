<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试功能页</title>
</head>
<body>
<h1>这是一个测试页</h1>
SimpleTag : ${SimpleTag("0.0")}
<hr/>
SimpleSub : ${SimpleSub("123456789", 3, 6)}
<hr/>
ArrayTagMethodModel : ${ArrayTags.ArrayTagMethodModel()}
<hr/>
ArrayTagDirectiveModel : <@ArrayTags.ArrayTagDirectiveModel name="喵喵喵" act="撸">0_0&nbsp;&nbsp;${paramList.act}&nbsp;&nbsp; ${paramList.name}</@ArrayTags.ArrayTagDirectiveModel>
<hr/>
ArrayTagDirectiveModel : <@ArrayTags.ArrayTagDirectiveModel name="汪汪汪" act="撸">-.-&nbsp;&nbsp;${paramList.act}&nbsp;&nbsp; ${paramList.name}</@ArrayTags.ArrayTagDirectiveModel>
<hr/>
<hr/>
CamelToUnderScoreTest 转下划线 ${"CamelToUnderScoreTest"?replace("([a-z])([A-Z]+)","$1_$2","r")}
<hr/>
camel_to_under_score_test 转驼峰 ${"camel_to_under_score_test"?replace("_+"," ","r")?capitalize?replace(" ", "", "r")?uncap_first}
</body>
</html>