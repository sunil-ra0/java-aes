// Function to fetch encrypted data from the backend
async function fetchEncryptedData() {
    try {
        const response = await fetch('http://localhost:8080/api/encrypt'); // Assuming backend is running on localhost:8080
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();

        const encryptedData = data.encryptedData;
        const aesKey = data.aesKey;

        const decryptedData = await decryptData(encryptedData, aesKey);
        document.getElementById('output').textContent = `Decrypted Data: ${decryptedData}`;
    } catch (error) {
        console.error('Error fetching or decrypting data:', error);
        document.getElementById('output').textContent = 'Error fetching or decrypting data. See console for details.';
    }
}

// Function to decrypt data using AES-GCM
async function decryptData(encryptedData, key) {
    try {
        const aesKey = await importKey(key);
        const encryptedArrayBuffer = base64ToArrayBuffer(encryptedData);
        const iv = new Uint8Array(encryptedArrayBuffer.slice(0, 12));
        const data = encryptedArrayBuffer.slice(12);

        const decrypted = await crypto.subtle.decrypt(
            {
                name: "AES-GCM",
                iv: iv
            },
            aesKey,
            data
        );

        const decoder = new TextDecoder();
        return decoder.decode(decrypted);
    } catch (error) {
        console.error('Error decrypting data:', error);
        return 'Error decrypting data. See console for details.';
    }
}

// Function to import AES key from base64 encoded string
async function importKey(base64Key) {
    try {
        const keyBuffer = base64ToArrayBuffer(base64Key);
        return crypto.subtle.importKey(
            "raw",
            keyBuffer,
            "AES-GCM",
            true,
            ["decrypt"]
        );
    } catch (error) {
        console.error('Error importing AES key:', error);
        return Promise.reject('Error importing AES key. See console for details.');
    }
}

// Function to convert base64 string to ArrayBuffer
function base64ToArrayBuffer(base64) {
    const binaryString = window.atob(base64);
    const len = binaryString.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes.buffer;
}
