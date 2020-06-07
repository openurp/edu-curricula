[#ftl]
[@b.head/]
[@b.grid items=awardLabelTypes var="awardLabelType"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="25%" property="code" title="序号"/]
    [@b.col width="25%" property="name" title="名称"/]
    [@b.col width="20%" property="beginOn" title="生效时间"/]
    [@b.col width="20%" property="endOn" title="失效时间"/]
  [/@]
[/@]
[@b.foot/]
