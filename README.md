# JWTAnalyzer

JWTAnalyzer is a JavaFX desktop application for decoding and optionally verifying JSON Web Tokens (JWT). It provides a simple interface to inspect the header and payload of JWTs, as well as view system resource usage.

## Features

- **JWT Input**  
  Paste or type your JWT into the input area for decoding.

- **Verification (Optional)**  
  - Enter a shared secret or public key to verify the token.  
  - Select the algorithm (currently supports `HS256`).

- **Decoded Output**  
  - View the decoded JWT **header** and **payload** in separate, read-only areas.  
  - Both areas support multiple lines and automatically expand with the window.

- **Controls**  
  - `Decode` button: Decode the JWT and display the contents.  
  - `Verify` button: Verify the JWT signature using the provided key and algorithm.  
  - `Clear` button: Clear all input and output fields.

- **Status and System Information**  
  - Status label shows the current application state.  
  - Footer displays CPU usage, memory usage, and the current time.

## Usage

1. Paste a JWT into the **JWT Token Input** area.  
2. Optionally provide a secret/public key and select the algorithm.  
3. Click **Decode** to view the header and payload.  
4. Click **Verify** to validate the token signature.  
5. Use **Clear** to reset all fields.

## Requirements

- Java 25 or higher
- JavaFX 25 or compatible runtime  
- Maven (for building the project)  

<img width="1082" height="832" alt="image" src="https://github.com/user-attachments/assets/1cf473c7-2b9f-4880-b3e8-5dfa3dfc9258" />

## Build and Run

```bash
# Clone the repository
git clone https://github.com/USERNAME/JwtAnalyzer.git
cd JwtAnalyzer

# Build with Maven
mvn clean install

# Run the application
mvn javafx:run
```
