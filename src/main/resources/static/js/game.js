class PingPongGame {
    constructor() {
        this.canvas = document.getElementById('gameCanvas');
        this.ctx = this.canvas.getContext('2d');
        this.stompClient = null;
        this.gameState = null;
        this.keys = {};
        this.playerSide = null; // Will be assigned when player joins
        this.lastActionTime = 0;
        this.actionThrottle = 50; // ms between actions
        
        this.init();
    }

    init() {
        this.setupWebSocket();
        this.setupKeyboardControls();
        this.assignPlayerSide();
        this.startGameLoop();
    }

    assignPlayerSide() {
        // Simple assignment - first player gets left, second gets right
        // In a real game, you'd want proper lobby management
        this.playerSide = Math.random() > 0.5 ? 'LEFT' : 'RIGHT';
        console.log('Assigned to player side:', this.playerSide);
    }

    setupWebSocket() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);
        
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            this.updateConnectionStatus('Подключено', 'connected');
            
            this.stompClient.subscribe('/topic/gameState', (message) => {
                this.gameState = JSON.parse(message.body);
                this.updateUI();
            });
        }, (error) => {
            console.log('Connection error: ' + error);
            this.updateConnectionStatus('Отключено', 'disconnected');
        });
    }

    setupKeyboardControls() {
        document.addEventListener('keydown', (event) => {
            this.keys[event.key] = true;
            this.handleKeyPress();
            event.preventDefault();
        });

        document.addEventListener('keyup', (event) => {
            this.keys[event.key] = false;
            event.preventDefault();
        });
    }

    handleKeyPress() {
        const currentTime = Date.now();
        if (currentTime - this.lastActionTime < this.actionThrottle) {
            return;
        }

        let action = null;
        
        if (this.playerSide === 'LEFT') {
            if (this.keys['w'] || this.keys['W']) {
                action = 'UP';
            } else if (this.keys['s'] || this.keys['S']) {
                action = 'DOWN';
            }
        } else if (this.playerSide === 'RIGHT') {
            if (this.keys['ArrowUp']) {
                action = 'UP';
            } else if (this.keys['ArrowDown']) {
                action = 'DOWN';
            }
        }

        if (action && this.stompClient && this.stompClient.connected) {
            this.stompClient.send('/app/playerAction', {}, JSON.stringify({
                action: action,
                player: this.playerSide
            }));
            this.lastActionTime = currentTime;
        }
    }

    startGameLoop() {
        const gameLoop = () => {
            this.render();
            requestAnimationFrame(gameLoop);
        };
        gameLoop();
    }

    render() {
        if (!this.gameState) return;

        // Clear canvas
        this.ctx.fillStyle = '#000';
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);

        // Draw center line
        this.ctx.strokeStyle = '#fff';
        this.ctx.lineWidth = 2;
        this.ctx.setLineDash([10, 10]);
        this.ctx.beginPath();
        this.ctx.moveTo(this.canvas.width / 2, 0);
        this.ctx.lineTo(this.canvas.width / 2, this.canvas.height);
        this.ctx.stroke();
        this.ctx.setLineDash([]);

        // Draw paddles
        this.ctx.fillStyle = '#fff';
        
        // Left paddle
        const leftPaddle = this.gameState.leftPaddle;
        this.ctx.fillRect(leftPaddle.x, leftPaddle.y, leftPaddle.width, leftPaddle.height);
        
        // Right paddle
        const rightPaddle = this.gameState.rightPaddle;
        this.ctx.fillRect(rightPaddle.x, rightPaddle.y, rightPaddle.width, rightPaddle.height);

        // Draw ball
        const ball = this.gameState.ball;
        this.ctx.beginPath();
        this.ctx.arc(ball.x, ball.y, ball.radius, 0, Math.PI * 2);
        this.ctx.fill();

        // Highlight current player's paddle
        if (this.playerSide === 'LEFT') {
            this.ctx.strokeStyle = '#ffeb3b';
            this.ctx.lineWidth = 3;
            this.ctx.strokeRect(leftPaddle.x - 2, leftPaddle.y - 2, 
                              leftPaddle.width + 4, leftPaddle.height + 4);
        } else if (this.playerSide === 'RIGHT') {
            this.ctx.strokeStyle = '#ffeb3b';
            this.ctx.lineWidth = 3;
            this.ctx.strokeRect(rightPaddle.x - 2, rightPaddle.y - 2, 
                              rightPaddle.width + 4, rightPaddle.height + 4);
        }
    }

    updateUI() {
        if (!this.gameState) return;

        document.getElementById('leftScore').textContent = this.gameState.leftScore;
        document.getElementById('rightScore').textContent = this.gameState.rightScore;
    }

    updateConnectionStatus(text, status) {
        const statusElement = document.getElementById('connectionStatus');
        statusElement.textContent = text;
        statusElement.className = 'connection-status ' + status;
    }
}

// Start the game when page loads
document.addEventListener('DOMContentLoaded', () => {
    new PingPongGame();
});
