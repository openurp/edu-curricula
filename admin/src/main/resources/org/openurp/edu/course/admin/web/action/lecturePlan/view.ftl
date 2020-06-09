[#if lecturePlan??]
<!DOCTYPE html>
<html dir="ltr" mozdisallowselectionprint moznomarginboxes>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="google" content="notranslate">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>PDF.js viewer</title>
  <link rel="stylesheet" href="${b.static_url('pdfjs','web/viewer.css')}">
  <link rel="resource" type="application/l10n" href="${b.static_url('pdfjs','web/locale/locale.properties')}">
  <script src="${b.static_url('pdfjs','build/pdf.js')}"></script>
</head>

<body tabindex="1" class="loadingInProgress">

<canvas id="the-canvas"></canvas>

<script>
  PDFJS.workerSrc="${b.static_url('pdfjs','build/pdf.worker.js')}"
  var url = ${url};

  var loadingTask = PDFJS.getDocument(url);
  loadingTask.promise.then(function(pdf) {

    pdf.getPage(1).then(function(page) {
      var scale = 1.5;
      var viewport = page.getViewport(scale);
      var canvas = document.getElementById('the-canvas');
      var context = canvas.getContext('2d');
      canvas.height = viewport.height;
      canvas.width = viewport.width;

      var renderContext = {
        canvasContext: context,
        viewport: viewport
      };
      var renderTask = page.render(renderContext);
      renderTask.then(function () {
        console.log('Page rendered');
      });
    });
  }, function (reason) {
    console.error(reason);
  });
</script>
</body>
</html>
[#else]
  找不到附件
[/#if]
