import * as React from 'react';
import { Upload, X, File } from 'lucide-react';
import { cn } from '@/utils/cn';
import { Button } from '@/components/ui/Button';
import { formatFileSize } from '@/utils/format';

interface FileUploadProps {
  accept?: string;
  maxSize?: number; // in bytes
  multiple?: boolean;
  value?: File[];
  onChange?: (files: File[]) => void;
  onError?: (error: string) => void;
  disabled?: boolean;
  className?: string;
}

export const FileUpload = React.forwardRef<HTMLInputElement, FileUploadProps>(
  (
    {
      accept,
      maxSize,
      multiple = false,
      value = [],
      onChange,
      onError,
      disabled,
      className,
    },
    // ref
  ) => {
    const [dragActive, setDragActive] = React.useState(false);
    const inputRef = React.useRef<HTMLInputElement>(null);

    const handleDrag = (e: React.DragEvent) => {
      e.preventDefault();
      e.stopPropagation();
      if (e.type === 'dragenter' || e.type === 'dragover') {
        setDragActive(true);
      } else if (e.type === 'dragleave') {
        setDragActive(false);
      }
    };

    const validateFile = (file: File): string | null => {
      if (maxSize && file.size > maxSize) {
        return `File size exceeds ${formatFileSize(maxSize)}`;
      }
      if (accept) {
        const acceptedTypes = accept.split(',').map((t) => t.trim());
        const fileExtension = '.' + file.name.split('.').pop()?.toLowerCase();
        const mimeType = file.type;

        const isValid = acceptedTypes.some(
          (type) =>
            type === fileExtension ||
            type === mimeType ||
            (type.endsWith('/*') &&
              mimeType.startsWith(type.replace('/*', '')))
        );

        if (!isValid) {
          return `File type not accepted. Accepted types: ${accept}`;
        }
      }
      return null;
    };

    const handleFiles = (files: FileList | null) => {
      if (!files) return;

      const newFiles: File[] = [];
      const errors: string[] = [];

      Array.from(files).forEach((file) => {
        const error = validateFile(file);
        if (error) {
          errors.push(`${file.name}: ${error}`);
        } else {
          newFiles.push(file);
        }
      });

      if (errors.length > 0) {
        onError?.(errors.join('\n'));
      }

      if (newFiles.length > 0) {
        const updatedFiles = multiple ? [...value, ...newFiles] : newFiles;
        onChange?.(updatedFiles);
      }

      setDragActive(false);
    };

    const handleDrop = (e: React.DragEvent) => {
      e.preventDefault();
      e.stopPropagation();
      if (!disabled) {
        handleFiles(e.dataTransfer.files);
      }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      if (!disabled) {
        handleFiles(e.target.files);
      }
    };

    const removeFile = (index: number) => {
      const newFiles = value.filter((_, i) => i !== index);
      onChange?.(newFiles);
    };

    const openFileDialog = () => {
      inputRef.current?.click();
    };

    return (
      <div className={cn('space-y-4', className)}>
        <div
          className={cn(
            'relative rounded-lg border-2 border-dashed p-8 text-center transition-colors',
            dragActive && 'border-primary bg-primary/5',
            disabled && 'cursor-not-allowed opacity-50',
            !disabled && 'cursor-pointer hover:border-primary'
          )}
          onDragEnter={handleDrag}
          onDragLeave={handleDrag}
          onDragOver={handleDrag}
          onDrop={handleDrop}
          onClick={disabled ? undefined : openFileDialog}
        >
          <input
            ref={inputRef}
            type="file"
            className="hidden"
            accept={accept}
            multiple={multiple}
            onChange={handleChange}
            disabled={disabled}
          />

          <Upload className="mx-auto h-10 w-10 text-muted-foreground" />
          <p className="mt-2 text-sm font-medium">
            Drop files here or click to browse
          </p>
          <p className="mt-1 text-xs text-muted-foreground">
            {accept && `Accepted formats: ${accept}`}
            {maxSize && ` • Max size: ${formatFileSize(maxSize)}`}
          </p>
        </div>

        {value.length > 0 && (
          <div className="space-y-2">
            {value.map((file, index) => (
              <div
                key={index}
                className="flex items-center justify-between rounded-md border p-3"
              >
                <div className="flex items-center gap-3">
                  <File className="h-5 w-5 text-muted-foreground" />
                  <div className="min-w-0 flex-1">
                    <p className="truncate text-sm font-medium">
                      {file.name}
                    </p>
                    <p className="text-xs text-muted-foreground">
                      {formatFileSize(file.size)}
                    </p>
                  </div>
                </div>
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  onClick={() => removeFile(index)}
                  disabled={disabled}
                >
                  <X className="h-4 w-4" />
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>
    );
  }
);

FileUpload.displayName = 'FileUpload';