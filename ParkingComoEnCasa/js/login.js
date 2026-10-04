document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    // Toggle mostrar/ocultar contraseña
    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
        });
    }

    // Validación de formulario
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const correo = document.getElementById('correo');
            const password = document.getElementById('password');
            const errorCorreo = document.getElementById('error-correo');
            const errorPassword = document.getElementById('error-password');
            
            let valido = true;

            // Validar correo
            if (!correo.value || !correo.value.includes('@')) {
                errorCorreo.classList.add('visible');
                correo.classList.add('error');
                valido = false;
            } else {
                errorCorreo.classList.remove('visible');
                correo.classList.remove('error');
            }

            // Validar contraseña
            if (!password.value || password.value.length < 6) {
                errorPassword.classList.add('visible');
                password.classList.add('error');
                valido = false;
            } else {
                errorPassword.classList.remove('visible');
                password.classList.remove('error');
            }

            if (valido) {
                window.location.href = 'dashboard.html';
            }
        });

        // Limpiar errores al escribir
        ['correo', 'password'].forEach(id => {
            const input = document.getElementById(id);
            if (input) {
                input.addEventListener('input', function() {
                    this.classList.remove('error');
                    document.getElementById('error-' + id).classList.remove('visible');
                });
            }
        });
    }
});