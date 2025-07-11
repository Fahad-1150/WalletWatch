Database setup

create database walletwatch;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);


**PAGES OF WalletWatch:**

🧾 Log In Page: User can log in using Username and Password.
🎨 Styled with modern UI, gradient buttons, and clean layout.

<img width="749" height="375" alt="{BB9216C1-097A-49B3-BE8C-AD2D296526B9}" src="https://github.com/user-attachments/assets/97b8d831-c124-480f-b52a-f19a9318248d" />


 Sign-Up option for new user registration.

<img width="748" height="375" alt="{8B9F2E84-0CE6-447C-B2D4-6821273223BD}" src="https://github.com/user-attachments/assets/fdb4d89c-8b19-4863-96e6-75c678ee15e8" />

📊 Dashboard: After log-in, users land here to view all expense and income details.
📈 Displays data in table, line-chart (category-wise), and pie-chart (income vs expense).
🔄 Includes filtering by date and category with real-time summary updates.
🔘 Left-side buttons let users view, add, edit, and update data by category (Education, Living, Food, etc.).

<img width="750" height="394" alt="{D2F71A87-AD1F-4779-991A-3761B35BB818}" src="https://github.com/user-attachments/assets/db56e62b-9aad-48b4-9d9c-45385658e679" />



