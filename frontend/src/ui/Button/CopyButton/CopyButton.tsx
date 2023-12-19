import React, { useState } from 'react';
import styles from '../Button.module.css';

interface CopyButtonProps {
    copyText: string;
}

const CopyButton: React.FC<CopyButtonProps> = ({ copyText }) => {
    const [copied, setCopied] = useState(false);
    const copyToClipboard = () => {
        navigator.clipboard.writeText(copyText)
        .then(() => {
            setCopied(true);
        })
        .catch(err => {
            console.error('Failed to copy text: ', err);
        });
    };
    return (
        <button 
            onClick={copyToClipboard} 
            className={`${styles.button} ${copied ? styles.active : ''}`}
        >
            {copied ? "Event ID Copied!" : "Copy Event ID for Inviting Others"}
        </button>
    );
}

export default CopyButton;
