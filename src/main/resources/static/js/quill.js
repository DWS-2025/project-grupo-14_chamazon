document.addEventListener('DOMContentLoaded', function() {
    // We initialize Quill over the div #editor
    var quill = new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{ header: [1, 2, false] }],
                ['bold', 'italic', 'underline'],
                ['link', 'blockquote', 'code-block', 'image'],
                [{ list: 'ordered' }, { list: 'bullet' }]
            ]
        }
    });


    var existing = document.querySelector('#contenido').value;
    if (existing) {
        quill.root.innerHTML = existing;
    }

    // When the user clicks the "Submit" button, we save the HTML contents on the hidden field
    var form = document.querySelector('form');
    form.addEventListener('submit', function() {
        document.querySelector('#contenido').value = quill.root.innerHTML;
    });
});
