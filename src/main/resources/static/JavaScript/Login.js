document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    if (!email || !password) {
        alert('Por favor, completa todos los campos.');
        return;
    }
    if (email === 'admin@techgear.com' && password === '123456') {
        alert('¡Login exitoso! Bienvenido a TechGear. Redirigiendo al panel de usuario...');
        const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
        modal.hide();
        document.querySelector('.nav-item:last-child button').innerHTML = '<i class="fas fa-sign-out-alt me-1"></i>Cerrar Sesión';
    } else {
        alert('Credenciales incorrectas. Usa: admin@techgear.com / 123456 para demo.');
    }
});