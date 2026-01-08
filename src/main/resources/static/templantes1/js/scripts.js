document.addEventListener('DOMContentLoaded', function() {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    function addToCart(product, price) {
        const existingProduct = cart.find(item => item.product === product);
        if (existingProduct) {
            existingProduct.quantity += 1;
        } else {
            cart.push({ product, price, quantity: 1 });
        }
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        showAlert('Producto agregado al carrito!', 'success');
    }

    function updateCartDisplay() {
        const cartItems = document.getElementById('cartItems');
        const cartTotal = document.getElementById('cartTotal');
        if (!cartItems || !cartTotal) return;

        if (cart.length === 0) {
            cartItems.innerHTML = '<p class="text-muted">No hay productos en el carrito.</p>';
            cartTotal.textContent = '0';
            return;
        }

        cartItems.innerHTML = '';
        let total = 0;
        cart.forEach((item, index) => {
            const itemTotal = item.price * item.quantity;
            total += itemTotal;
            const listItem = document.createElement('div');
            listItem.className = 'list-group-item d-flex justify-content-between align-items-center';
            listItem.innerHTML = `
                <div>
                    <h6 class="mb-1">${item.product}</h6>
                    <small>Precio: $${item.price} x ${item.quantity}</small>
                </div>
                <div>
                    <span class="badge bg-primary rounded-pill">$${itemTotal.toFixed(2)}</span>
                    <button class="btn btn-sm btn-outline-danger ms-2 remove-item" data-index="${index}">×</button>
                </div>
            `;
            cartItems.appendChild(listItem);
        });
        cartTotal.textContent = total.toFixed(2);

        document.querySelectorAll('.remove-item').forEach(button => {
            button.addEventListener('click', function() {
                const index = parseInt(this.getAttribute('data-index'));
                cart.splice(index, 1);
                localStorage.setItem('cart', JSON.stringify(cart));
                updateCartDisplay();
                showAlert('Producto eliminado del carrito', 'warning');
            });
        });
    }

    function showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.top = '20px';
        alertDiv.style.right = '20px';
        alertDiv.style.zIndex = '1050';
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.body.appendChild(alertDiv);
        setTimeout(() => alertDiv.remove(), 3000);
    }

    // Eventos para botones "Agregar al carrito"
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', () => {
            const product = button.getAttribute('data-product');
            const price = parseFloat(button.getAttribute('data-price'));
            addToCart(product, price);
        });
    });

    // Más funciones para login, registro, contacto y usuarios (como expliqué arriba)...

    updateCartDisplay();
    // También se llama a funciones para mostrar usuarios, etc.
});
