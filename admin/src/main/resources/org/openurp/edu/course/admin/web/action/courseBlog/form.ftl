[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
${b.css("kindeditor","themes/default/default.css")}
${b.script("kindeditor","kindeditor-all-min.js")}
${b.script("kindeditor","lang/zh-CN.js")}
[@b.toolbar title="课程资料维护"]bar.addBack();[/@]
  [#assign sa][#if courseBlog.persisted]!update?id=${courseBlog.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list" enctype="multipart/form-data" onsubmit="syncEditor" name="blogForm"]
      [#if courseBlog.persisted]
        [@b.field label="学年学期"]${courseBlog.semester.schoolYear}学年${courseBlog.semester.name}学期[/@]
        [@b.field label="课程"]${courseBlog.course.name}(${courseBlog.course.code})[/@]
        [@b.select name="courseBlog.meta.courseGroup.id" label="课程分类" ]
          <option value="">...</option>
          [#list courseGroups as courseGroup]
            <option value="${courseGroup.id}" [#if courseBlog.meta?? && courseBlog.meta.courseGroup?? && courseBlog.meta.courseGroup == courseGroup]selected[/#if]  [#if courseGroup.children?size>0]disabled[/#if]>
              [#if (courseGroup.indexno?split('.'))?size == 2]&nbsp;&nbsp;
              [#elseif (courseGroup.indexno?split('.'))?size == 3]&nbsp;&nbsp;&nbsp;&nbsp;
              [/#if]
              ${courseGroup.name}
            </option>
          [/#list]
        [/@]
      [#else]
        [@edu.semester name="courseBlog.semester.id" label="学年学期"  value=currentSemester required="true"/]
        [@b.field label="选择课程" required='true']
          <select id="courses" name="courseBlog.course" style="width:200px;" >
            <option value="${(courseBlog.course.code)!}" selected>${(courseBlog.course.name)!}${(courseBlog.course.code)!}</option>
          </select>
        [/@]
      [/#if]
      [@b.textarea label="中文简介" name="courseBlog.description" value=(courseBlog.description)! id="description" cols="100" rows="10" required="true" maxlength="10000"/]
      [@b.textarea label="英文简介" name="courseBlog.enDescription" value=(courseBlog.enDescription)! id="enDescription" cols="100" rows="10" required="true" maxlength="10000"/]
      [@b.field label="教学大纲" required="true"]
        <input name="syllabus.attachment" type="file" style="display:inline-block" id="syllabus"/> <label id="syllabusSpan" > </label><span style="color:red;font-weight: 700" >注：请上传pdf格式的文件</span>
        [#if courseBlog?? && syllabuses ?? && syllabuses?size>0]
          [#list syllabuses as syllabus]
            [#if syllabus.attachment??]
              <br>
              <label id="syllabusAttaLabel" class="form-check-label">
              已有附件：${(syllabus.attachment.name)!}
                [@b.a target="_blank" href="syllabus!attachment?id=${syllabus.id}"]下载[/@]&nbsp;&nbsp;
                [@b.a target="_blank" href="syllabus!view?id=${syllabus.id}"]预览[/@]&nbsp;&nbsp;
                [@b.a onclick="removeSyllabusAtta('${syllabus.id}');return false"]删除[/@]
              </label>
              [#if syllabus_has_next]<br>[/#if]
            [/#if]
          [/#list]
        [/#if]
      [/@]
      [@b.field label="授课计划" required="true" ]
        <input name="lecturePlan.attachment" type="file" style="display:inline-block" id="lecturePlan"/> <label id="lecturePlanSpan"> </label> <span style="color:red;font-weight: 700" >注：请上传pdf格式的文件</span>
        [#if courseBlog.persisted && lecturePlans?? && lecturePlans?size>0 ]
          [#list lecturePlans as lecturePlan]
            [#if lecturePlan.attachment??]
              <br>
              <label id="lecturePlanAttaLabel" class="form-check-label">
                已有附件：${(lecturePlan.attachment.name)!}
                [@b.a target="_blank" href="lecture-plan!attachment?id=${lecturePlan.id}"]下载[/@]&nbsp;&nbsp;
                [@b.a target="_blank" href="lecture-plan!view?id=${lecturePlan.id}"]预览[/@]&nbsp;&nbsp;
                [@b.a onclick="removePlanAtta('${lecturePlan.id}');return false"]删除[/@]
              </label>
              [#if lecturePlan_has_next]<br>[/#if]
            [/#if]
          [/#list]
        [/#if]
      [/@]
      [@b.textfield label="预修课程" name="courseBlog.preCourse" value=(courseBlog.preCourse)! style="width:600px"  required="true" comment='<span style="color:red" ><b>注：没有预修课程请填“无”</b></span>' /]
      [@b.textarea label="教材和参考书目" name="courseBlog.books" value=(courseBlog.books)! id="books" required="true" maxlength="10000"/]
      [@b.field label="教学资料"]
        <input name="materialAttachment" type="file" style="display:inline-block" id="materialAttachment"/>
        <label id="materialAttachmentSpan"></label><span style="color:red;font-weight: 700" >注：可根据具体情况将电子教案、习题、试卷等课程教学资料打包上传，不超过50MB</span>
        [#if courseBlog.materialAttachment??]
          <br>
          <label id="materialAttaLabel" class="form-check-label">
            已有附件：${(courseBlog.materialAttachment.name)!}
            [@b.a target="_blank" href="teacher!attachment?id=${courseBlog.id}"]下载[/@]&nbsp;&nbsp;
            [@b.a onclick="removeMaterialAtta('${courseBlog.id}');return false"]删除[/@]
          </label>
        [/#if]
      [/@]
      [@b.textfield label="课程网站地址" name="courseBlog.website" value=(courseBlog.website)! style="width:250px"  comment='<span style="color:red" ><b>注：请填写一个课程网站地址，如有多个课程网站，可填入备注栏</b></span>'/]
      [@b.field label="获奖情况"]
        [#list labelTypes!?sort_by("code") as awardLabelType]
          <input type="checkBox" name="awardLabelTypeId" value="${awardLabelType.id}" [#if choosedType?? && choosedType?seq_contains(awardLabelType)]checked[/#if]>${awardLabelType.name}&nbsp;
        [/#list]
      [/@]
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
            [#list years?reverse as year]
              <option value="${year}" [#if yearMap?? && yearMap.get(awardLabelType)?? && yearMap.get(awardLabelType)==year]selected[/#if]>${year}</option>
            [/#list]
          </select>
          [#list awardMap.get(awardLabelType)!?sort_by("code") as awardLabel]
            <input type="checkBox" name="${awardLabelType.id}_awardLabelId" value="${awardLabel.id}" [#if choosedLabel?? && choosedLabel?seq_contains(awardLabel)]checked[/#if]>${awardLabel.name}&nbsp;
          [/#list]
        [/@]
      [/#list]
      [@b.textfield label="备注" name="courseBlog.remark" value=(courseBlog.remark)! style="width:600px" /]
      [@b.formfoot]
        [#if courseBlog.persisted]<input type="hidden" name="courseBlog.id" value="${courseBlog.id!}" />[/#if]
        [@b.submit value="确定" /]
        <span style="color:red;font-weight: 700" >注：如果点击提交后无反应，可能是有文件尚未上传完毕，请不要关闭浏览器，稍作等待。</span>
      [/@]
    [/@]

<script>
  jQuery(function() {
    jQuery("#courses").ajaxChosen(
            {
              method: 'POST',
              url: '${b.url("course-or-user-search!courseAjax")}',
              postData:function(){
                return {
                  pageIndex:1,
                  pageSize:10,
                }
              }
            }
            , function(data) {
              var dataObj=eval("(" + data + ")");
              var items = {};
              jQuery.each(dataObj.courses, function(i, course) {
                items[course.code] = course.name + '(' + course.code + ')';
              });
              return items;
            });

  })

  var descriptionEditor;
  var enDescriptionEditor;
  var booksEditor;
  jQuery(document).ready(function (){
    descriptionEditor = KindEditor.create('textarea[name="courseBlog.description"]', {
      resizeType : 1,
      allowPreviewEmoticons : false,
      allowImageUpload : false,
      allowFileManager:false,
      items:[
        'undo', 'redo', '|', 'preview','cut', 'copy', 'paste',
        'plainpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
        'superscript', 'selectall', '|', 'fullscreen', '/',
        'formatblock', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'table'
      ],
      afterBlur:function () {
        $('#description').val(descriptionEditor.html());
      }
    });
    enDescriptionEditor = KindEditor.create('textarea[name="courseBlog.enDescription"]', {
      resizeType : 1,
      allowPreviewEmoticons : false,
      allowImageUpload : false,
      allowFileManager:false,
      items:[
        'undo', 'redo', '|', 'preview','cut', 'copy', 'paste',
        'plainpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
        'superscript', 'selectall', '|', 'fullscreen', '/',
        'formatblock', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'table'
      ],
      afterBlur:function () {
        $('#enDescription').val(enDescriptionEditor.html());
      }
    });
    booksEditor = KindEditor.create('textarea[name="courseBlog.books"]', {
      resizeType : 1,
      allowPreviewEmoticons : false,
      allowImageUpload : false,
      allowFileManager:false,
      items:[
        'undo', 'redo', '|', 'preview','cut', 'copy', 'paste',
        'plainpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
        'superscript', 'selectall', '|', 'fullscreen', '/',
        'formatblock', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'table'
      ],
      afterBlur:function () {
      $('#books').val(booksEditor.html());
    }
    });
  });

  function syncEditor(){
    if($('#syllabus').val()=="" && $('#syllabusAttaLabel').length==0 ){
      $('#syllabusSpan').html("请上传教学大纲");
      $('#syllabusSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }
    if (!$('#syllabus').val()=="" && !$('#syllabus').val().endsWith("pdf")){
      $('#syllabusSpan').html("请上传pdf格式的文件");
      $('#syllabusSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }
    if($('#lecturePlan').val()=="" && $('#lecturePlanAttaLabel').length==0 ){
      $('#lecturePlanSpan').html("请上传授课计划");
      $('#lecturePlanSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }
    if (!$('#lecturePlan').val()=="" && !$('#lecturePlan').val().endsWith("pdf")){
      $('#lecturePlanSpan').html("请上传pdf格式的文件");
      $('#lecturePlanSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }

    var files = document.getElementById("materialAttachment").files;
    if (files.length>0 && files[0].size > 50000000){
      $('#materialAttachmentSpan').html("文件不超过50MB");
      $('#materialAttachmentSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }
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

  function removeSyllabusAtta(id){
    var url = "${b.url('syllabus!removeAtta?id=aaa')}";
    var newUrl = url.replace("aaa",id);
    if (confirm("是否确认删除附件？")) {
      $.ajax({
        "type": "post",
        "url": newUrl,
        "async": false,
        "success": function () {
          $('#syllabusAttaLabel').remove();
        }
      });
    }
  }
  function removePlanAtta(id){
    var url = "${b.url('syllabus!removeAtta?id=aaa')}";
    var newUrl = url.replace("aaa",id);
    if (confirm("是否确认删除附件？")) {
      $.ajax({
        "type": "post",
        "url": newUrl,
        "async": false,
        "success": function () {
          $('#lecturePlanAttaLabel').remove();
        }
      });
    }
  }
  function removeMaterialAtta(id){
    var url = "${b.url('course-blog!removeAtta?id=aaa')}";
    var newUrl = url.replace("aaa",id);
    if (confirm("是否确认删除附件？")) {
      $.ajax({
        "type": "post",
        "url": newUrl,
        "async": false,
        "success": function () {
          $('#materialAttaLabel').remove();
        }
      });
    }
  }
</script>
[@b.foot/]