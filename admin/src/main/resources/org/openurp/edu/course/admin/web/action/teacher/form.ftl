[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
${b.css("kindeditor","themes/default/default.css")}
${b.script("kindeditor","kindeditor-all-min.js")}
[#--${b.script("kindeditor","lang/zh-CN.js")}--]
[@b.toolbar title="课程资料维护"]bar.addBack();[/@]
  [#assign sa][#if courseBlog.persisted]!update?id=${courseBlog.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list" enctype="multipart/form-data"  onsubmit="syncEditor" name="blogForm"]
      [@b.field label="学年学期"]${courseBlog.semester.schoolYear}学年${courseBlog.semester.name}学期[/@]
      [@b.field label="课程"]${courseBlog.course.name}(${courseBlog.course.code})[/@]
      [@b.select name="courseBlog.meta.courseGroup.id" label="课程分组"]
        <option value="">...</option>
        [#list courseGroups as courseGroup]
          <option value="${courseGroup.id}" [#if meta.courseGroup?? && meta.courseGroup == courseGroup]selected[/#if]>
            [#if (courseGroup.indexno?split('.'))?size == 2]&nbsp;&nbsp;
            [#elseif (courseGroup.indexno?split('.'))?size == 3]&nbsp;&nbsp;&nbsp;&nbsp;
            [/#if]
            ${courseGroup.name}
          </option>
        [/#list]
      [/@]
      [@b.textarea label="中文简介(必填)" name="courseBlog.description" value=(courseBlog.description)! id="description" cols="100" rows="15"  maxlength="10000"/]
      [@b.textarea label="英文简介" name="courseBlog.enDescription" value=(courseBlog.enDescription)! id="enDescription" cols="100" rows="15" maxlength="10000"/]
      [@b.textfield label="预修课程" name="courseBlog.preCourse" value=(courseBlog.preCourse)! style="width:700px"/]
      [@b.textfield label="教材和辅助资料" name="courseBlog.materials" value=(courseBlog.materials)! style="width:250px"/]
      [@b.textfield label="课程网站" name="courseBlog.website" value=(courseBlog.website)! style="width:250px"/]
      [@b.field label="教学大纲附件" required="true"]
        <input name="syllabus.attachment" type="file" style="display:inline-block"/> <span style="color:red" >注：请上传pdf格式的文件</span>
        [#if courseBlog?? && syllabuses ?? && syllabuses?size>0]
          [#list syllabuses as syllabus]
            [#if syllabus.attachment??]
              <br>已有附件：${(syllabus.attachment.name)!}
              [@b.a target="_blank" href="syllabus!attachment?id=${syllabus.id}"]下载[/@]
              &nbsp;&nbsp;[@b.a target="_blank" href="syllabus!view?id=${syllabus.id}"]预览[/@]
              [#if syllabus_has_next]<br>[/#if]
            [/#if]
          [/#list]
        [/#if]
      [/@]
      [@b.field label="授课计划附件" required="true"]
        <input name="lecturePlan.attachment" type="file" style="display:inline-block"/> <span style="color:red" >注：请上传pdf格式的文件</span>
        [#if courseBlog?? && lecturePlans?? && lecturePlans?size>0 ]
          [#list lecturePlans as lecturePlan]
            [#if lecturePlan.attachment??]
              <br>已有附件：${(lecturePlan.attachment.name)!}
              [@b.a target="_blank" href="lecture-plan!attachment?id=${lecturePlan.id}"]下载[/@]
              &nbsp;&nbsp;[@b.a target="_blank" href="lecture-plan!view?id=${lecturePlan.id}"]预览[/@]
              [#if lecturePlan_has_next]<br>[/#if]
            [/#if]
          [/#list]
        [/#if]
      [/@]
      [@b.field label="获奖情况"]
        [#list labelTypes!?sort_by("code") as awardLabelType]
          <input type="checkBox" name="awardLabelTypeId" value="${awardLabelType.id}" [#if choosedType?? && choosedType?seq_contains(awardLabelType)]checked[/#if]>${awardLabelType.name}&nbsp;
        [/#list]
      [/@]
      [#assign years=['2010','2011','2012','2013','2014','2015','2016','2017','2018','2019','2020','2021']]
      [#list labelTypes!?sort_by("code") as awardLabelType]
        [#assign aa]
          [#if choosedType?? && choosedType?seq_contains(awardLabelType)]
            display:block
          [#else]
            display:none
          [/#if]
        [/#assign]
        [@b.field label="${awardLabelType.name}" name="${awardLabelType.id}" style=aa]
          <select name="${awardLabelType.id}_year" style="width:80px">
            <option value="">...</option>
            [#list years as year]
              <option value="${year}" [#if yearMap?? && yearMap.get(awardLabelType)?? && yearMap.get(awardLabelType)==year]selected[/#if]>${year}</option>
            [/#list]
          </select>
          [#list awardMap.get(awardLabelType)!?sort_by("code") as awardLabel]
            <input type="checkBox" name="${awardLabelType.id}_awardLabelId" value="${awardLabel.id}" [#if choosedLabel?? && choosedLabel?seq_contains(awardLabel)]checked[/#if]>${awardLabel.name}&nbsp;
          [/#list]
        [/@]
      [/#list]
      [@b.formfoot]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
      [/@]
    [/@]
<script>
  var descriptionEditor;
  var enDescriptionEditor;
  jQuery(document).ready(function (){
    descriptionEditor = KindEditor.create('textarea[name="courseBlog.description"]', {
      resizeType : 1,
      allowPreviewEmoticons : false,
      allowImageUpload : false,
      allowFileManager:false
    });
    enDescriptionEditor = KindEditor.create('textarea[name="courseBlog.enDescription"]', {
      resizeType : 1,
      allowPreviewEmoticons : false,
      allowImageUpload : false,
      allowFileManager:false
    });
  });

  function syncEditor(){
    $('#description').val(descriptionEditor.html());
    $('#enDescription').val(enDescriptionEditor.html());
    return true;
  }

  var formObj = $("form[name=blogForm]");
  formObj.find("input[name='awardLabelTypeId']").click(function () {

    var item = formObj.find("li[name="+this.value+"]");

    if (item.css("display") == "none")
      item.css("display", "block");
    else {
      item.css("display", "none");
      item.children().val("");
      item.children().prop("checked", false);
    }
  })
</script>
[@b.foot/]