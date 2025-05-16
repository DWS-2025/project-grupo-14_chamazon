
function initQuill() {
    // We initialize Quill over the div #editor
    var quill = new Quill('#editor', {
        theme: 'snow'
    });

    // Get the hidden field and, if previous content exists, assign it to the editor
    var hiddenInput = document.getElementById('contenido');
    if (hiddenInput.value.trim()) {
        quill.root.innerHTML = hiddenInput.value;
    }

    // Select the container element by getting form action's id
    var form = document.getElementById('CommentFormQJS');
    if (form) {
        form.addEventListener('submit', function() {
            // Update the hidden field
            hiddenInput.value = quill.root.innerHTML;
            console.log("Contenido del editor:", hiddenInput.value);
        });
    } else {
        console.error('Formulario no encontrado.');
    }
}

if (document.readyState !== 'loading') {
    initQuill();
} else {
    document.addEventListener('DOMContentLoaded', initQuill);
}