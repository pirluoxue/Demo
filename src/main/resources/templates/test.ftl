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
ArrayTagDirectiveModel : <@ArrayTags.ArrayTagDirectiveModel name="喵喵喵" act="撸">0_0&nbsp;&nbsp;${paramList.act}&nbsp;&nbsp; ${paramList.name}</@ArrayTags.ArrayTagDirectiveModel>
</body>
</html>