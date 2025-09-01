document.addEventListener('DOMContentLoaded', function () {
    const checkoutButton = document.getElementById('checkout-button');
    const orderHistoryButton = document.getElementById('order-history-button');
    const orderDetailsContainer = document.getElementById('order-details');
    const orderHistoryContainer = document.getElementById('order-history');

    if (checkoutButton) {
        checkoutButton.addEventListener('click', function () {
            fetch('/api/orders/checkout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token') 
                }
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
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
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
                    </div>
                `;
            });
        }
        orderHistoryContainer.innerHTML = historyHtml;
    }
});