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
      [@b.select name="courseBlog.meta.courseGroup.id" label="课程分类"]
        <option value="">...</option>
        [#list courseGroups as courseGroup]
          <option value="${courseGroup.id}" [#if meta.courseGroup?? && meta.courseGroup == courseGroup]selected[/#if] [#if courseGroup.children?size>0]disabled[/#if]>
            [#if (courseGroup.indexno?split('.'))?size == 2]&nbsp;&nbsp;
            [#elseif (courseGroup.indexno?split('.'))?size == 3]&nbsp;&nbsp;&nbsp;&nbsp;
            [/#if]
            ${courseGroup.name}
          </option>
        [/#list]
      [/@]
      [@b.textarea label="中文简介" name="courseBlog.description" value=(courseBlog.description)! id="description" maxlength="10000" required="true" ][/@]
      [@b.textarea label="英文简介" name="courseBlog.enDescription" value=(courseBlog.enDescription)! id="enDescription" maxlength="10000" required="true" /]
      [@b.field label="教学大纲" required="true"]
        <input name="syllabus.attachment" type="file" style="display:inline-block" id="syllabus"/>
        [@b.a onclick="removeSyllabus();return false" id="syllabusA" ]清除[/@]
        <label id="syllabusSpan" > </label>
        <span style="color:red;font-weight: 700" >注：请上传pdf格式的文件</span>
        [#if courseBlog?? && syllabus ?? ]
          [#if syllabus.attachment??]
            <br>
            <label id="syllabusAttaLabel" class="form-check-label">
            已有附件：${(syllabus.attachment.name)!}
              [@b.a target="_blank" href="syllabus!attachment?id=${syllabus.id}"]下载[/@]&nbsp;&nbsp;
              [@b.a target="_blank" href="syllabus!view?id=${syllabus.id}"]预览[/@]&nbsp;&nbsp;
              [@b.a onclick="removeSyllabusAtta('${syllabus.id}');return false"]删除[/@]
            </label>
          [/#if]
        [/#if]
      [/@]
      [@b.field label="授课计划" required="true" ]
        <input name="lecturePlan.attachment" type="file" style="display:inline-block" id="lecturePlan"/>
        [@b.a onclick="removeLecturePlan();return false" id="lecturePlanA" ]清除[/@]
        <label id="lecturePlanSpan"> </label>
        <span style="color:red;font-weight: 700" >注：请上传pdf格式的文件</span>
        [#if courseBlog.persisted && lecturePlan??]
          [#if lecturePlan.attachment??]
            <br>
            <label id="lecturePlanAttaLabel" class="form-check-label">
              已有附件：${(lecturePlan.attachment.name)!}
              [@b.a target="_blank" href="lecture-plan!attachment?id=${lecturePlan.id}"]下载[/@]&nbsp;&nbsp;
              [@b.a target="_blank" href="lecture-plan!view?id=${lecturePlan.id}"]预览[/@]&nbsp;&nbsp;
              [@b.a onclick="removePlanAtta('${lecturePlan.id}');return false"]删除[/@]
            </label>
          [/#if]
        [/#if]
      [/@]
      [@b.textfield label="预修课程" name="courseBlog.preCourse" value=(courseBlog.preCourse)! style="width:600px"  required="true" onblur="saveAttribute('preCourse',this.value)" comment='<span style="color:red" ><b>注：没有预修课程请填“无”</b></span>' /]
      [@b.textarea label="教材和参考书目" name="courseBlog.books" value=(courseBlog.books)! id="books" required="true" maxlength="10000"/]
      [@b.field label="教学资料"]
        <input name="materialAttachment" type="file" style="display:inline-block" id="materialAttachment"/>
        [@b.a onclick="removeMaterial();return false" id="materialA" ]清除[/@]
        <label id="materialAttachmentSpan"></label>
        <span style="color:red;font-weight: 700" >注：可根据具体情况将电子教案、习题、试卷等课程教学资料打包上传，不超过50MB</span>
        [#if courseBlog.materialAttachment??]
          <br>
          <label id="materialAttaLabel" class="form-check-label">
            已有附件：${(courseBlog.materialAttachment.name)!}
            [@b.a target="_blank" href="teacher!attachment?id=${courseBlog.id}"]下载[/@]&nbsp;&nbsp;
            [@b.a onclick="removeMaterialAtta('${courseBlog.id}');return false"]删除[/@]
          </label>
        [/#if]
      [/@]
      [@b.textfield label="课程网站地址" name="courseBlog.website" value=(courseBlog.website)! style="width:250px"  onblur="saveAttribute('website',this.value)" comment='<span style="color:red" ><b>注：请填写一个课程网站地址，如有多个课程网站，可填入备注栏</b></span>'/]
      [@b.field label="获奖情况"]
        [#if meta??]
          [#list meta.awards! as award]
            ${award.awardLabel.name},${award.year}年[#if award_has_next]<br>[/#if]
          [/#list]
          [#if meta.awards?size>0]<br>[/#if]
        [/#if]
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
      [@b.textfield label="备注" name="courseBlog.remark" value=(courseBlog.remark)! style="width:600px" onblur="saveAttribute('remark',this.value)"/]
      [@b.formfoot]
        [@b.submit value="确定"/]
        <span style="color:red;font-weight: 700" >注：如果点击确定后无反应，可能是有文件尚未上传完毕，请不要关闭浏览器，稍作等待。</span>
      [/@]
    [/@]
<script>
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
        saveAttribute("description", $('#description').val())
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
        saveAttribute("enDescription", $('#enDescription').val())
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
        saveAttribute("books", $('#books').val())
    }
    });
  });
  //提交前检查
  function syncEditor(){
    if($('#syllabus').val()=="" &&  $('#syllabusAttaLabel').length==0){
      $('#syllabusSpan').html("请上传教学大纲");
      $('#syllabusSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }
    if (!$('#syllabus').val()=="" && !$('#syllabus').val().endsWith("pdf")){
      $('#syllabusSpan').html("请上传pdf格式的文件");
      $('#syllabusSpan').css({"background-image":"url('${b.static_url('bui','images/arrow.gif')}')","background-position":"left center","padding":"2px","padding-left":"18px","color":"#fff"})
      return false;
    }
    if($('#lecturePlan').val()=="" && $('#lecturePlanAttaLabel').length==0){
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

  //清除上传但未提交的附件
  $('#syllabusA').attr("style", "display:none;");
  $('#syllabus').change(function () {
    $('#syllabusA').attr("style", "display:inline;");
  });
  function removeSyllabus(){
    $('#syllabus').val("");
    $('#syllabusA').attr("style", "display:none;");
  }

  $('#lecturePlanA').attr("style", "display:none;");
  $('#lecturePlan').change(function () {
    $('#lecturePlanA').attr("style", "display:inline;");
  });
  function removeLecturePlan(){
    $('#lecturePlan').val("");
    $('#lecturePlanA').attr("style", "display:none;");
  }

  $('#materialA').attr("style", "display:none;");
  $('#materialAttachment').change(function () {
    $('#materialA').attr("style", "display:inline;");
  });
  function removeMaterial(){
    $('#materialAttachment').val("");
    $('#materialA').attr("style", "display:none;");
  }

  //附件添加删除功能
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
    var url = "${b.url('lecture-plan!removeAtta?id=aaa')}";
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

  //焦点离开即保存
  function saveAttribute(name,value) {
    if(name && value){
      $.ajax({
        "type": "post",
        "url": "${b.url("course-blog!saveAttribute")}",
        "data": {
          "id": ${courseBlog.id},
          "name": name,
          "value":value
        },
        "async": false,
      });
    }
  }
</script>
[@b.foot/]
