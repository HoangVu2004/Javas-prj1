document.addEventListener('DOMContentLoaded', function () {
    const checkoutButton = document.getElementById('checkout-button');
    const orderHistoryButton = document.getElementById('order-history-button');
    const orderDetailsContainer = document.getElementById('order-details');
    const orderHistoryContainer = document.getElementById('order-history');
    const totalKitsSpan = document.getElementById('total-kits');
    const totalAmountSpan = document.getElementById('total-amount');

    // Function to fetch and display cart summary
    function fetchCartSummary() {
        fetch('/api/cart', {
            method: 'GET',
            headers: {
                // 'Authorization': 'Bearer ' + localStorage.getItem('token') // Removed for demo
            }
        })
        .then(response => response.json())
        .then(cart => {
            let totalKits = 0;
            let totalAmount = 0;
            cart.items.forEach(item => {
                totalKits += item.quantity;
                totalAmount += item.quantity * item.kit.price;
            });
            totalKitsSpan.textContent = totalKits;
            totalAmountSpan.textContent = totalAmount.toFixed(2);
        })
        .catch(error => {
            console.error('Error fetching cart summary:', error);
            totalKitsSpan.textContent = 'Error';
            totalAmountSpan.textContent = 'Error';
        });
    }

    // Fetch cart summary on page load
    fetchCartSummary();

    if (checkoutButton) {
        checkoutButton.addEventListener('click', function () {
            const name = document.getElementById('name').value;
            const address = document.getElementById('address').value;
            const phone = document.getElementById('phone').value;
            const paymentMethod = document.getElementById('payment-method').value;

            if (!name || !address || !phone || !paymentMethod) {
                alert('Please fill in all shipping and payment details.');
                return;
            }

            const checkoutRequest = {
                name: name,
                address: address,
                phone: phone,
                paymentMethod: paymentMethod
            };

            fetch('/api/orders/checkout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // 'Authorization': 'Bearer ' + localStorage.getItem('token') // Removed for demo
                },
                body: JSON.stringify(checkoutRequest)
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Checkout failed');
                }
            })
            .then(order => {
                displayOrderDetails(order);
                fetchCartSummary(); // Refresh cart summary after checkout
            })
            .catch(error => {
                console.error('Error:', error);
                orderDetailsContainer.innerHTML = '<p>Checkout failed. Please try again.</p>';
            });
        });
    }

    if (orderHistoryButton) {
        orderHistoryButton.addEventListener('click', function () {
            fetch('/api/orders', {
                method: 'GET',
                headers: {
                    // 'Authorization': 'Bearer ' + localStorage.getItem('token') // Removed for demo
                }
            })
            .then(response => response.json())
            .then(orders => {
                displayOrderHistory(orders);
            })
            .catch(error => {
                console.error('Error:', error);
                orderHistoryContainer.innerHTML = '<p>Failed to load order history.</p>';
            });
        });
    }

    function displayOrderDetails(order) {
        let detailsHtml = `<h3>Order Successful!</h3>
            <p>Order ID: ${order.id}</p>
            <p>Order Date: ${new Date(order.orderDate).toLocaleString()}</p>
            <p>Total Amount: $${order.totalAmount.toFixed(2)}</p>
            <p>Status: ${order.status}</p>
            <p>Name: ${order.name}</p>
            <p>Address: ${order.address}</p>
            <p>Phone: ${order.phone}</p>
            <p>Payment Method: ${order.paymentMethod}</p>
            <h4>Items:</h4>`;
        
        order.orderItems.forEach(item => {
            detailsHtml += `<p>${item.kit.name} - Quantity: ${item.quantity} - Price: $${item.priceAtPurchase.toFixed(2)}</p>`;
        });

        orderDetailsContainer.innerHTML = detailsHtml;
    }

    function displayOrderHistory(orders) {
        let historyHtml = '<h3>Your Order History</h3>';
        if (orders.length === 0) {
            historyHtml += '<p>You have no past orders.</p>';
        } else {
            orders.forEach(order => {
                historyHtml += `
                    <div class="order-summary">
                        <p><strong>Order ID:</strong> ${order.id}</p>
                        <p><strong>Date:</strong> ${new Date(order.orderDate).toLocaleString()}</p>
                        <p><strong>Total:</strong> $${order.totalAmount.toFixed(2)}</p>
                        <p><strong>Status:</strong> ${order.status}</p>
                        <p><strong>Name:</strong> ${order.name}</p>
                        <p><strong>Address:</strong> ${order.address}</p>
                        <p><strong>Phone:</strong> ${order.phone}</p>
                        <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                    </div>
                `;
            });
        }
        orderHistoryContainer.innerHTML = historyHtml;
    }
});